//
//  EventManagerViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/6/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class EventManagerViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var eventNameEntry: UITextField!
    @IBOutlet weak var dateTimeEntry: UIDatePicker!
    @IBOutlet weak var deleteEventButton: UIButton!
    
    
    
    // MARK: - Class Properties
    var eventName: String?
    var dateTime: Date?
    var selectedEvent: Event?
    var parentMode = false
    var deleteEvent = false
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Unhide embedded navigation controller
        self.navigationController?.navigationBar.isHidden = false
        
        deleteEventButton.isHidden = true
        
        if parentMode == true {
            self.title = "Edit Event"
            eventNameEntry.text = selectedEvent?.name
            dateTimeEntry.date = (selectedEvent?.date)!
            deleteEventButton.isHidden = false
        }
        
        // Register keyboard notifications. This will be used later to move text fields when the keyboard is active.
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        let newDate = Event(name: "New Event", date: dateTimeEntry.date, image: #imageLiteral(resourceName: "Logo"), completion: false, recurrenceFrequency: 0, originalIndex: 0)
        let now = Event(name: "Now", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: false, recurrenceFrequency: 0, originalIndex: 0)
        
        // If event name entry is invalid, let the user know and prevent segue
        if eventNameEntry.text == "" || (eventNameEntry.text?.count)! > 15 {
            let alert = UIAlertController(title: "Invalid Event Name", message: "Please enter an event name that is 15 characters or less.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If entered date is in the past, let the user know and prevent segue
        if newDate.date < now.date {
            let alert = UIAlertController(title: "Invalid Event Date", message: "Please do not select a date and time in the past.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
        
        else {
            return true
        }
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Reference destination view controller
        if let destination = segue.destination as? EventDetailsViewController {
            if let eventNameInput = eventNameEntry.text {
                eventName = eventNameInput
                dateTime = dateTimeEntry.date
            }
            
            if parentMode == true {
                selectedEvent?.name = eventName!
                selectedEvent?.date = dateTime!
                destination.selectedEvent = selectedEvent
                destination.parentMode = parentMode
                
                if selectedEvent?.recurrenceFrequency == 0 {
                    destination.oneTimeEvent = true
                }
                
                else {
                    destination.oneTimeEvent = false
                }
            }
            
            // Pass necessary information to destination
            destination.eventName = eventName
            destination.dateTime = dateTime
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        // Force application to unfocus from current text field
        view.endEditing(true)
        
        // Call this function into action when touch is detected
        super.touchesBegan(touches, with: event)
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func deleteButtonTapped(_ sender: UIButton) {
        let alert = UIAlertController(title: "Delete Event?", message: "Are you sure you want to permanently remove this event? All events in the series will also be removed.", preferredStyle: .alert)
        
        let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        
        let deleteButton = UIAlertAction(title: "Delete", style: UIAlertActionStyle.destructive) { (deleteSelectedEvent) in
            self.deleteEvent = true
            self.performSegue(withIdentifier: "DeleteFromEventManager", sender: nil)
        }
        
        alert.addAction(cancelButton)
        alert.addAction(deleteButton)
        
        self.present(alert, animated: true, completion: nil)
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        guard let userInfo = notification.userInfo
            
            else {
                return
        }
        
        guard let keyboardSize = userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue
            
            else {
                return
        }
        
        let keyboardFrame = keyboardSize.cgRectValue
        
        if self.view.frame.origin.y == 0{
            self.view.frame.origin.y -= keyboardFrame.height
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        if self.view.frame.origin.y != 0{
            self.view.frame.origin.y = 0
        }
    }
}
