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
        // Do any additional setup after loading the view, typically from a nib.
        
        testDisplay()
        
        if joy != nil {
            updateDisplay()
        }
    }
    
    
    
    // MARK: - Custom Functions
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
    
    func updateDisplay() {
        giveJoyDisplay.text = joy?.displayGiven()
        getJoyDisplay.text = joy?.displayReceived()
        payItForwardDisplay.text = joy?.displayPayItForward()
    }
}
