//
//  ScheduleViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit
import Firebase
import FirebaseFirestore
import FirebaseStorage

class ScheduleViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var editButton: UIBarButtonItem!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var navigationBar: UINavigationBar!
    
    
    
    // MARK: - Class Properties
    var cellCount = 1
    var tempEvents: [Event] = []
    var filteredEvents: [[Event]?] = []
    var dates: [Date] = []
    var dateComponents: [(month: Int, day: Int, year: Int)] = []
    var userEmail: String?
    var userPassword: String?
    var parentCode: String?
    var userID: String?
    var events: [Event] = []
    var currentUser: User?
    var parentMode = false
    var selectedEvent: Event?
    var deleteEvent = false
    var editingOn = false
    var newUser = false
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Listen for changes in user authentication
        Auth.auth().addStateDidChangeListener { (auth, user) in
            if user != nil {
                
                // If user is new, create a new database entry for them
                let database = Firestore.firestore()
                
                self.userID = user?.uid
                
                if self.newUser != false {
                    database.collection("users").document(self.userEmail!).setData([
                        "userEmail" : self.userEmail!,
                        "userID" : self.userID!,
                        "parentCode" : self.parentCode!]) { err in
                            if let err = err {
                                print("Error writing document: \(err)")
                            } else {
                                print("Document successfully written!")
                            }
                    }
                    
                    self.currentUser = User(userEmail: self.userEmail!, userPassword: self.userPassword!, parentCode: self.parentCode!, userID: self.userID!, events: [])
                    
                    self.userEmail = self.currentUser?.userEmail
                    self.userPassword = self.currentUser?.userPassword
                    self.parentCode = self.currentUser?.parentCode
                    self.userID = self.currentUser?.userID
                    
                    if let userEvents = self.currentUser?.events {
                        self.events = userEvents
                    }
                }
                    
                // If user is not new, load user data and stored event data
                else {
                    let docRef = database.collection("users").document(self.userEmail!)
                    
                    docRef.getDocument { (document, error) in
                        if let document = document, document.exists {
                            let id = document.get("userID") as? String
                            let code = document.get("parentCode") as? String
                            
                            self.currentUser = User(userEmail: self.userEmail!, userPassword: self.userPassword!, parentCode: code!, userID: id!, events: [])
                            
                            self.userEmail = self.currentUser?.userEmail
                            self.userPassword = self.currentUser?.userPassword
                            self.parentCode = self.currentUser?.parentCode
                            self.userID = self.currentUser?.userID
                            
                            if let userEvents = self.currentUser?.events {
                                self.events = userEvents
                            }
                            
                            database.collection("users").document(self.userEmail!).collection("events").getDocuments() { (querySnapshot, err) in
                                if let err = err {
                                    print("Error getting documents: \(err)")
                                } else {
                                    for document in querySnapshot!.documents {
                                        let date = document.get("date") as? Date
                                        let imageRef = document.get("imageRef") as? String
                                        let isComplete = document.get("isComplete") as? Bool
                                        let name = document.get("name") as? String
                                        let originalIndex = document.get("originalIndex") as? Int
                                        let recurrenceFrequency = document.get("recurrenceFrequency") as? Int
                                        let requiresCompletion = document.get("requiresCompletion") as? Bool
                                        
                                        let storage = Storage.storage()
                                        let imageReference = storage.reference(withPath: imageRef!)
                                        
                                        imageReference.getData(maxSize: 1 * 1024 * 1024) { data, error in
                                            if error != nil {
                                                let newEvent = Event(name: name!, date: date!, image: #imageLiteral(resourceName: "Logo"), requiresCompletion: requiresCompletion!, recurrenceFrequency: recurrenceFrequency!, originalIndex: originalIndex!, isComplete: isComplete!)
                                                
                                                print("Created event with default image: \(error!)")
                                                
                                                self.events.append(newEvent)
                                                self.currentUser?.events.append(newEvent)
                                                
                                                self.filterEvents()
                                                self.tableView.reloadData()
                                            }
                                            
                                            else {
                                                let image = UIImage(data: data!)
                                                
                                                let newEvent = Event(name: name!, date: date!, image: image!, requiresCompletion: requiresCompletion!, recurrenceFrequency: recurrenceFrequency!, originalIndex: originalIndex!, isComplete: isComplete!)
                                                
                                                self.events.append(newEvent)
                                                self.currentUser?.events.append(newEvent)
                                                
                                                self.filterEvents()
                                                self.tableView.reloadData()
                                            }
                                        }
                                    }
                                }
                            }
                            
                        } else {
                            print("Document does not exist")
                        }
                    }
                }
            }
        }
        
        // Hide embedded navigation controller to properly display custom navigation bar
        self.navigationController?.navigationBar.isHidden = true
        
        // Register xib
        let headerNib = UINib.init(nibName: "WeekHeader", bundle: nil)
        tableView.register(headerNib, forHeaderFooterViewReuseIdentifier: "header_01")
        
        // Call custom function to filter existing events
        filterEvents()
        
        // Reload tableview
        tableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Hide embedded navigation controller to properly display custom navigation bar
        self.navigationController?.navigationBar.isHidden = true
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        var shouldContinue = false
        
        // Prompt for Parent Code before allowing user to create new events
        let alert = UIAlertController(title: "Parent Code Required", message: "Please enter your 4-digit Parent Code to continue.", preferredStyle: .alert)
        
        let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        let okButton = UIAlertAction(title: "OK", style: .default) { (parentCode) in
            let parentCodeEntry = alert.textFields![0] as UITextField
            
            if parentCodeEntry.text! == self.parentCode {
                self.performSegue(withIdentifier: "EventManagerSegue", sender: self)
                
                shouldContinue = true
            }
                
            // If Parent Code is invalid, let the user know
            else {
                let alert = UIAlertController(title: "Invalid Code", message: "Oops! The code you entered was not correct.", preferredStyle: .alert)
                
                let okButton = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                alert.addAction(okButton)
                
                self.present(alert, animated: true)
                
                shouldContinue = false
            }
        }
        
        alert.addAction(cancelButton)
        alert.addAction(okButton)
        alert.addTextField { (textField) in
            textField.placeholder = "Enter Parent Code..."
            textField.textAlignment = .center
        }
        
        self.present(alert, animated: true)
        
        return shouldContinue
    }
    
    
    
    // MARK: - Table view data source
    // Determine number of rows in each section according to number of level two elements in each level one element in filtered array
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let filteredEvents = filteredEvents[section] {
            return filteredEvents.count
        }
            
        else {
            return 0
        }
    }
    
    // Determine number of sections to display according to level one elements in filtered array
    func numberOfSections(in tableView: UITableView) -> Int {
        return filteredEvents.count
    }
    
    // Clear default header titles
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return nil
    }
    
    // Set height for custom cells
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 75
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var returnCell: EventCell?
        
        // Loop through custom cells. This provides cells of multiple colors to easier differentiate from individual events
        switch cellCount {
        case 1:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_01", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_01", for: indexPath)
            }
            
            returnCell = cell
            
        case 2:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_02", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_02", for: indexPath)
            }
            
            returnCell = cell
            
        case 3:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_03", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_03", for: indexPath)
            }
            
            returnCell = cell
            
        case 4:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_04", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_04", for: indexPath)
            }
            
            returnCell = cell
            
        default:
            print("Invalid")
        }
        
        // Reference the current event to be populated
        let currentEvent = filteredEvents[indexPath.section]![indexPath.row]
        
        // Populate current cell with appropriate information
        returnCell?.eventImage.image = currentEvent.image
        
        // Hide image view if no photo has been taken for the event in question
        if currentEvent.image == #imageLiteral(resourceName: "Logo") {
            returnCell?.eventImage.isHidden = true
        }
          
        // Otherwise, display the selected photo
        else {
            returnCell?.eventImage.isHidden = false
        }
        
        returnCell?.eventNameLabel.text = currentEvent.name
        returnCell?.eventTimeLabel.text = currentEvent.time()
        
        if indexPath.section == 0 {
            currentEvent.isComplete = events[currentEvent.originalIndex].isComplete
        }
        
        if currentEvent.requiresCompletion == true {
            if currentEvent.isComplete == false {
                returnCell?.taskCompletionIndicator.image = #imageLiteral(resourceName: "Incomplete")
                returnCell?.taskCompletionIndicator.isHidden = false
                returnCell?.taskCompletionIndicator.isUserInteractionEnabled = true
            }
                
            else if currentEvent.isComplete == true {
                if indexPath.section == 0 {
                    returnCell?.taskCompletionIndicator.image = #imageLiteral(resourceName: "Complete")
                    returnCell?.taskCompletionIndicator.isHidden = false
                    returnCell?.taskCompletionIndicator.isUserInteractionEnabled = false
                }
            }
        }
            
        else {
            returnCell?.taskCompletionIndicator.isHidden = true
        }
        
        // Helps to keep track of which custom cell to use next
        if cellCount == 4 {
            cellCount = 1
        }
            
        else {
            cellCount += 1
        }
        
        return returnCell!
    }
    
    // Set height for custom headers
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }
    
    // Populate custom headers
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var returnHeader: UIView?
        
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: "header_01") as? WeeklyHeaderView
        
        header?.headerTitleLabel.text = filteredEvents[section]?.last?.weekDay()
        header?.dateLabel.text = (filteredEvents[section]?.last?.monthName())! + " " + (filteredEvents[section]?.last?.day().description)!
        header?.yearLabel.text = filteredEvents[section]?.last?.year().description
        
        returnHeader = header
        
        return returnHeader
    }
    
    // Allows user to delete events by swiping or enter into a specific event to change its values
    // Changes are applied to the entire series of events
    // Requires Parent Code entry
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if parentMode == true {
            if editingStyle == .delete {
                let alert = UIAlertController(title: "Delete Event?", message: "Are you sure you want to permanently remove this event? All events in the series will also be removed.", preferredStyle: .alert)
                
                let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
                
                let deleteButton = UIAlertAction(title: "Delete", style: UIAlertActionStyle.destructive) { (deleteEvent) in
                    
                    // Delete the row from the data source
                    self.deleteCloudEvents()
                    
                    self.events.remove(at: self.filteredEvents[indexPath.section]![indexPath.row].originalIndex)
                    
                    self.filterEvents()
                    
                    UIView.transition(with: tableView, duration: 1.0, options: .transitionCrossDissolve, animations: {self.tableView.reloadData()}, completion: nil)
                }
                
                alert.addAction(cancelButton)
                alert.addAction(deleteButton)
                
                self.present(alert, animated: true, completion: nil)
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        // If Parent Mode is active, allow user to edit selected event
        if parentMode == true {
            editingOn = true
            selectedEvent = filteredEvents[indexPath.section]![indexPath.row]
            self.performSegue(withIdentifier: "EventManagerSegue", sender: nil)
        }
            
        // If Parent Mode is not active, allow user to mark a task as complete
        // This is only applied to tasks on the current day to prevent the child from marking future tasks as complete before they have had a chance to actually do them
        else if parentMode == false {
            editingOn = false
            selectedEvent = filteredEvents[indexPath.section]![indexPath.row]
            if indexPath.section == 0 {
                if events[(selectedEvent?.originalIndex)!].isComplete == false {
                    events[(selectedEvent?.originalIndex)!].isComplete = true
                    
                    filteredEvents[indexPath.section]![indexPath.row].isComplete = true
                    
                    filterEvents()
                    tableView.reloadData()
                }
            }
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Reference destination view and pass required data
        if let destination = segue.destination as? EventManagerViewController {
            if editingOn == true {
                destination.selectedEvent = selectedEvent
                destination.parentMode = parentMode
            }
        }
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func editButtonTapped(_ sender: UIBarButtonItem) {
        
        // Call custom function to toggle Parent Mode on and off
        toggleParentMode()
    }
    
    @IBAction func returnFromEventManager(segue: UIStoryboardSegue) {
        
        // Reference sending view. Add newly created event and filter into the current schedule
        if let source = segue.source as? EventPhotoViewController {
            if parentMode == true && editingOn == true {
                selectedEvent = source.selectedEvent
                events[(selectedEvent?.originalIndex)!] = selectedEvent!
                
                filterEvents()
                
                tableView.reloadData()
                
                editingOn = false
            }
                
            else {
                if let newEvent = source.newEvent {
                    events.append(newEvent)
                    
                    filterEvents()
                    
                    tableView.reloadData()
                }
            }
        }
    }
    
    // Calls when the Delete Event button is tapped in the Event Manager view
    @IBAction func deleteFromEventManager(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventManagerViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
    
    // Calls when the Delete Event button is tapped in the Event Details view
    @IBAction func deleteFromEventDetails(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventDetailsViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
    
    // Calls when the Delete Event button is tapped in the Event Photo view
    @IBAction func deleteFromEventPhoto(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventPhotoViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
    
    // Allows child to mark taks on the current day as completed
    @IBAction func completionIndicatorTapped(recognizer: UITapGestureRecognizer) {
        let completionIndicator = recognizer.view as? UIImageView
        
        if completionIndicator?.image == #imageLiteral(resourceName: "Incomplete") {
            completionIndicator?.image = #imageLiteral(resourceName: "Complete")
            completionIndicator?.isUserInteractionEnabled = false
        }
    }
}
