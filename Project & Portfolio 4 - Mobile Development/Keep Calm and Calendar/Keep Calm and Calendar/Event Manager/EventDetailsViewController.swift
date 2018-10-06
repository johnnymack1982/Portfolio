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
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
