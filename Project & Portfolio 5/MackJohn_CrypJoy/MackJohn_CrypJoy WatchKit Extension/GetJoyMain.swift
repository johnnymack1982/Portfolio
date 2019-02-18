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
        
        if let context: Joy = context as? Joy {
            self.joy = context
            updateDisplay()
        }
        
        setTitle("")
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
        
        joy = globalJoy
        
        checkProgress()
        updateDisplay()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        return self.joy
    }
    
    
    
    // MARK: - Custom Functions
    func updateDisplay() {
        if let joy = joy {
            getJoyDisplay.setTitle(joy.displayReceived())
        }
    }
    
    func checkProgress() {
        let giveGoal = joy!.readGiveGoal()
        
        if giveGoal < 9 {
            getJoyDisplay.setEnabled(true)
        }
            
        else {
            getJoyDisplay.setEnabled(false)
        }
    }
}
