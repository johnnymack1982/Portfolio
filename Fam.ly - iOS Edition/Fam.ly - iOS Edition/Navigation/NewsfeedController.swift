//
//  NewsfeedController.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 5/22/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class NewsfeedController: UIViewController {
    
    
    
    // UI Outlets
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var postButton: UIButton!
    
    
    
    // System generated methods
    override func viewDidLoad() {
        super.viewDidLoad()

        cancelButton.isHidden = true
        postButton.isHidden = true
    }
}
