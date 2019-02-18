//
//  InterfaceController.swift
//  MackJohn_CrypJoy WatchKit Extension
//
//  Created by Johnny Mack on 2/14/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import WatchKit
import Foundation
import WatchConnectivity



// MARK: - Global Variables
var globalJoy: Joy?


class InterfaceController: WKInterfaceController, WCSessionDelegate {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var giveJoyDisplay: WKInterfaceButton!
    
    
    
    // MARK: - Class properties
    fileprivate let session: WCSession? = WCSession.isSupported() ? WCSession.default: nil
    var joy: Joy?
    
    
    
    // MARK: - Initializer
    override init() {
        super.init()
        
        // Set session delegate and activate
        session?.delegate = self
        session?.activate()
    }
    
    
    
    // MARK: - System generated functions
    override func awake(withContext context: Any?) {
        super.awake(withContext: context)
        
        if let context: Joy = context as? Joy {
            self.joy = context
            globalJoy = self.joy
            print(self.joy?.displayGiven())
        }
        
        if let globalJoy = globalJoy {
            joy = globalJoy
        }
        
        updateDisplay()
        setTitle("")
    }
    
    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }
    
    override func willDisappear() {
        globalJoy = joy
    }
    
    override func didDeactivate() {
        // This method is called when watch view controller is no longer visible
        super.didDeactivate()
    }
    
    @available(watchOS 2.2, *)
    public func session(_ session: WCSession, activationDidCompleteWith activationState: WCSessionActivationState, error: Error?) {        
        // Call custom method to get list of games to be populated
        getData()
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        return self.joy
    }
    
    
    
    // MARK: - Custom Functions
    func getData() {
        // Build message to send to iOS portion
        let joyValues: [String:Any] = ["updateData":true]
        
        // If a session is available, continue
        if let session = session, session.isReachable {
            
            // Send the message and handle reply from iOS portion
            session.sendMessage(joyValues, replyHandler: {
                replyData in
                
                print(replyData)
                
                DispatchQueue.main.async {
                    // If data is received from iOS portion, begin to decode
                    if let data = replyData["updatedData"] as? Data {
                        NSKeyedUnarchiver.setClass(Joy.self, forClassName: "Joy")
                        
                        do {
                            // Attempt to decode list of games
                            guard let joyObject = try NSKeyedUnarchiver.unarchiveTopLevelObjectWithData(data) as? Joy
                                
                                else {
                                    print("GIVEJOYMAIN: Error getting data from phone. Creating dummy data")
                                    
                                    self.joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                                    globalJoy = self.joy
                                    
                                    self.updateDisplay()
                                    
                                    return
                            }
                            
                            // Reference decoded game list
                            self.joy = joyObject
                        }
                            
                        catch {
                            fatalError("Can't unarchive data: \(error)")
                        }
                    }
                }
            }) { (error) in
                print(error.localizedDescription)
            }
        }
    }
    
    func updateDisplay() {
        if let joy = joy {
            giveJoyDisplay.setTitle(joy.displayGiven())
        }
    }
}
