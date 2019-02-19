//
//  GetJoyMain.swift
//  MackJohn_CrypJoy WatchKit Extension
//
//  Created by Johnny Mack on 2/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import WatchKit
import Foundation


class GetJoyMain: WKInterfaceController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var getJoyDisplay: WKInterfaceButton!
    
    
    
    // MARK: - Class Properties
    var joy: Joy?
    
    
    
    // MARK: - System Generated Functions
    override func awake(withContext context: Any?) {
        super.awake(withContext: context)
        
        // Reference Joy object from sending controller
        if let context: Joy = context as? Joy {
            self.joy = context
            
            // Call custom function to update UI with current values
            updateDisplay()
        }
        
        // Clear navigation item
        setTitle("")
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
        
        // Reference current value of global Joy object
        joy = globalJoy
        
        // Call custom function to check the users progress and enable/disable buttons as necessary
        checkProgress()
        
        // Call custom function to update UI with current values
        updateDisplay()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        // Send current Joy values to next controller
        return self.joy
    }
    
    
    
    // MARK: - Custom Functions
    // Custom function to update UI with current values
    func updateDisplay() {
        if let joy = joy {
            getJoyDisplay.setTitle(joy.displayReceived())
        }
    }
    
    // Custom function to check the users progress and enable/disable buttons as necessary
    func checkProgress() {
        // Reference current Give Joy goal
        let giveGoal = joy!.readGiveGoal()
        
        // If the current goal is less than 9, allow user to log more
        if giveGoal < 9 {
            getJoyDisplay.setEnabled(true)
        }
            
        // Otherwise, disable the button
        // This prevents values from becoming too large to fit on the screen
        // This will also provide users with a consistent maximum goal to strive for on a daily basis
        else {
            getJoyDisplay.setEnabled(false)
        }
    }
}
