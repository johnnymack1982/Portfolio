//
//  ScheduleViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class ScheduleViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var editButton: UIBarButtonItem!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var navigationBar: UINavigationBar!
    
    
    
    // MARK: - Class Properties
    var cellCount = 1
    var events: [Event] = []
    var tempEvents: [Event] = []
    var filteredEvents: [[Event]?] = []
    var dates: [Date] = []
    var dateComponents: [(month: Int, day: Int, year: Int)] = []
    var parentCode: Int?
    var parentMode = false
    var selectedEvent: Event?
    var deleteEvent = false
    var editingOn = false
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
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
        let alert = UIAlertController(title: "Coming Soon", message: "This feature will eventually require you to enter your Parent Code. For now, you'll be allowed to continue as if a valid Parent Code has been entered.", preferredStyle: .alert)
        let okButton = UIAlertAction(title: "OK", style: .default, handler: {_ in self.performSegue(withIdentifier: "EventManagerSegue", sender: nil) })
        alert.addAction(okButton)
        self.present(alert, animated: true)
        
        return true
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
        
        if currentEvent.image == #imageLiteral(resourceName: "Logo") {
            returnCell?.eventImage.isHidden = true
        }
        
        else {
            returnCell?.eventImage.isHidden = false
        }
        
        returnCell?.eventNameLabel.text = currentEvent.name
        returnCell?.eventTimeLabel.text = currentEvent.time()
        
        if currentEvent.requiresCompletion == true {
            returnCell?.taskCompletionIndicator.image = #imageLiteral(resourceName: "Incomplete")
            returnCell?.taskCompletionIndicator.isHidden = false
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
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if parentMode == true {
            if editingStyle == .delete {
                let alert = UIAlertController(title: "Delete Event?", message: "Are you sure you want to permanently remove this event? All events in the series will also be removed.", preferredStyle: .alert)
                
                let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
                
                let deleteButton = UIAlertAction(title: "Delete", style: UIAlertActionStyle.destructive) { (deleteEvent) in
                    
                    // Delete the row from the data source
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
        if parentMode == true {
            editingOn = true
            selectedEvent = filteredEvents[indexPath.section]![indexPath.row]
            self.performSegue(withIdentifier: "EventManagerSegue", sender: nil)
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? EventManagerViewController {
            if editingOn == true {
                destination.selectedEvent = selectedEvent
                destination.parentMode = parentMode
            }
        }
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func editButtonTapped(_ sender: UIBarButtonItem) {
        let alert = UIAlertController(title: "Coming Soon", message: "This feature will eventually require you to enter your Parent Code. For now, you'll be allowed to continue as if a valid Parent Code has been entered.", preferredStyle: .alert)
        let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(okButton)
        self.present(alert, animated: true)
        
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
    
    @IBAction func deleteFromEventManager(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventManagerViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
    
    @IBAction func deleteFromEventDetails(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventDetailsViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
    
    @IBAction func deleteFromEventPhoto(segue: UIStoryboardSegue) {
        if let source = segue.source as? EventPhotoViewController {
            if source.deleteEvent == true {
                selectedEvent = source.selectedEvent
                deleteSelectedEvent()
                editingOn = false
            }
        }
    }
}
