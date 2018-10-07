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
    
    
    
    // MARK: - Class Properties
    var eventName: String?
    var dateTime: Date?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Unhide embedded navigation controller
        self.navigationController?.navigationBar.isHidden = false
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        let newDate = Event(name: "New Event", date: dateTimeEntry.date, image: #imageLiteral(resourceName: "Logo"), completion: false, recurrenceFrequency: 0)
        let now = Event(name: "Now", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: false, recurrenceFrequency: 0)
        
        // If event name entry is invalid, let the user know and prevent segue
        if eventNameEntry.text == "" || (eventNameEntry.text?.count)! > 15 {
            let alert = UIAlertController(title: "Invalid Event Name", message: "Please enter an event name that is 15 characters or less.", preferredStyle: .alert)
            let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
            alert.addAction(okButton)
            self.present(alert, animated: true)
            
            return false
        }
            
        // If entered date is in the past, let the user know and prevent segue
        else if newDate.day() < now.day() || newDate.month() < now.month() || newDate.year() < now.year() {
            let alert = UIAlertController(title: "Invalid Event Date", message: "Please do not select a date in the past.", preferredStyle: .alert)
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
            
            // Pass necessary information to destination
            destination.eventName = eventName
            destination.dateTime = dateTime
        }
    }
}
