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
        self.navigationController?.navigationBar.isHidden = false
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
