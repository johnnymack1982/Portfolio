//
//  EventPhotoViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/6/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class EventPhotoViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var eventNameLabel: UILabel!
    @IBOutlet weak var eventTimeLabel: UILabel!
    @IBOutlet weak var eventPhotoView: UIImageView!
    
    
    
    // MARK: - Class Properties
    var eventName: String?
    var dateTime: Date?
    var requiresCompletion = false
    var oneTimeEvent = false
    var recurrenceFrequency: Int?
    var eventPhoto: UIImage?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
