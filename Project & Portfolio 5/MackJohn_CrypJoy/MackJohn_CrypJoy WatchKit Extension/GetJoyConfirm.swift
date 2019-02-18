//
//  GetJoyConfirm.swift
//  MackJohn_CrypJoy WatchKit Extension
//
//  Created by Johnny Mack on 2/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import WatchKit
import Foundation


class GetJoyConfirm: WKInterfaceController {
    
    
    
    // MARK: - Class Properties
    var joy: Joy?
    
    
    
    // MARK: - System Generated Fu ctions
    override func awake(withContext context: Any?) {
        super.awake(withContext: context)
        
        if let context: Joy = context as? Joy {
            self.joy = context
        }
    }

    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }

    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        if segueIdentifier == "GetJoyConfirm" {
            self.joy?.joyReceived()
            return self.joy
        }
            
        else {
            return nil
        }
    }
}
