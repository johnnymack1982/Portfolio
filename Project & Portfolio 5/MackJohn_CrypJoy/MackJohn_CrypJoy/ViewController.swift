//
//  ViewController.swift
//  MackJohn_CrypJoy
//
//  Created by Johnny Mack on 2/14/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var giveJoyDisplay: UILabel!
    @IBOutlet weak var getJoyDisplay: UILabel!
    @IBOutlet weak var payItForwardDisplay: UILabel!
    
    
    
    // MARK: - Class Properties
    public var joy: Joy?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        // This line is for testing purposes only and should be commented out during normal use
//        testDisplay()
        
        // If the global Joy object is not nil, set the class property to match
        if let globalJoy = globalJoy {
            joy = globalJoy
        }
        
        // If the class Joy property is not nil, call custom function to update display with current values
        if joy != nil {
            updateDisplay()
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        print("View Appeared")
        
        // If the global Joy object is not nil, set the class property to match
        if let globalJoy = globalJoy {
            joy = globalJoy
        }
        
        // If the class property is not nil, call custom function to update display with current values
        if joy != nil {
            updateDisplay()
        }
    }
    
    
    
    // MARK: - Custom Functions
    // Custom function to populate Joy object with dummy data
    // This should be used for test purposes only
    func testDisplay() {
        joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
        
        joy?.joyGiven()
        joy?.joyGiven()
        joy?.joyReceived()
        
        joy?.joyGiven()
        joy?.joyReceived()
        
        joy?.joyGiven()
        joy?.joyGiven()
        joy?.joyReceived()
        
        joy?.joyGiven()
        
        let encodedObject = try? JSONEncoder().encode(joy)
        print(encodedObject as Any)
        
        updateDisplay()
    }
    
    // Custom function to update display with current values
    func updateDisplay() {
        giveJoyDisplay.text = joy?.displayGiven()
        getJoyDisplay.text = joy?.displayReceived()
        payItForwardDisplay.text = joy?.displayPayItForward()
    }
}
