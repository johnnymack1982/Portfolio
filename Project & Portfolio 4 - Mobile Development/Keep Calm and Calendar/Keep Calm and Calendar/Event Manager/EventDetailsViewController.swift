//
//  EventDetailsViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/6/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class EventDetailsViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var requiresCompletionEntry: UISwitch!
    @IBOutlet weak var oneTimeEventEntry: UISwitch!
    @IBOutlet weak var recurrenceFrequencyEntry: UISegmentedControl!
    @IBOutlet weak var recurrenceFrequencyLabel: UILabel!
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var eventTimeLabel: UILabel!
    
    
    
    // MARK: - Class Properties
    var requiresCompletion = false
    var oneTimeEvent = false
    var recurrenceFrequency: Int?
    var eventName: String?
    var dateTime: Date?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Populate header with new event name
        eventNameLabel.text = eventName
        
        // Populate header with new event time
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: dateTime!)
        let minute = calendar.component(.minute, from: dateTime!)
        
        if hour > 12 {
            eventTimeLabel.text = "\(hour - 12):\(String(format: "%02d", minute)) PM"
        }
            
        else {
            eventTimeLabel.text = "\(hour):\(String(format: "%02d", minute)) AM"
        }
        
        // Call custom function to toggle whether or not this is a one time event
        toggleOneTimeEvent()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        // Call custom function to toggle whether or not this is a one time event
        toggleOneTimeEvent()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        // Reference destination view controller
        if let destination = segue.destination as? EventPhotoViewController {
            
            // Reference user choice for whether or not the new event requires completion
            if requiresCompletionEntry.isOn {
                requiresCompletion = true
            }
            
            else {
                requiresCompletion = false
            }
            
            // Reference user choice for whether or not the new event is a one time event
            if oneTimeEventEntry.isOn {
                recurrenceFrequency = 0
            }
            
            // If event is not a one time event, reference user choice for recurrence frequency
            else {
                recurrenceFrequency = recurrenceFrequencyEntry.selectedSegmentIndex + 1
            }
            
            // Pass referenced information to destination view controller
            destination.requiresCompletion = requiresCompletion
            destination.oneTimeEvent = oneTimeEvent
            destination.recurrenceFrequency = recurrenceFrequency
            destination.eventName = eventName
            destination.dateTime = dateTime
        }
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func oneTimeEventSwitchTapped(_ sender: UISwitch) {
        
        // Call custom function to toggle whether or not this is a one time event
        toggleOneTimeEvent()
    }
    
    
    
    // MARK: - Custom Functions
    func toggleOneTimeEvent() {
        
        // If this is a one time event, hide recurrence frequency options
        if oneTimeEventEntry.isOn {
            recurrenceFrequencyLabel.isHidden = true
            recurrenceFrequencyEntry.isHidden = true
        }
            
        // If this is not a one time event, unhide recurrence frequency options
        else {
            recurrenceFrequencyEntry.isHidden = false
            recurrenceFrequencyLabel.isHidden = false
        }
    }
}
