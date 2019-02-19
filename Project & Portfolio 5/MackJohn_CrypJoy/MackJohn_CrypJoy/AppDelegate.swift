//
//  AppDelegate.swift
//  MackJohn_CrypJoy
//
//  Created by Johnny Mack on 2/14/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import WatchConnectivity

var globalJoy: Joy?

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    
    
    // MARK: - Class Properties
    var window: UIWindow?
    var session: WCSession? {
        didSet {
            if let session = session {
                // Set session delegate and activate
                session.delegate = self
                session.activate()
            }
        }
    }
    
    
    
    // MARK: - System Generated Functions
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // If watch connectivity session is supported, use default session
        if WCSession.isSupported() {
            session = WCSession.default
        }
        
        return true
    }
}



extension AppDelegate: WCSessionDelegate {
    
    
    
    // MARK: - System Generated Functions
    func sessionDidDeactivate(_ session: WCSession) {}
    
    func sessionDidBecomeInactive(_ session: WCSession) {}
    
    func session(_ session: WCSession, activationDidCompleteWith activationState: WCSessionActivationState, error: Error?) {}
    
    func session(_ session: WCSession, didReceiveMessage message: [String : Any], replyHandler: @escaping ([String : Any]) -> Void) {
    }
    
    // TODO: - This callback is currently not firing
    // This will require additional debugging to address
    func session(_ session: WCSession, didReceiveMessageData messageData: Data) {
        // Print data received from watch for logging
        print(messageData)
        
        DispatchQueue.main.async {
            print("Message received")
            
            // Set class to decode received data
            NSKeyedUnarchiver.setClass(Joy.self, forClassName: "Joy")
            
            do {
                // Attempt to decode data into readable Joy object
                guard let joyObject = try NSKeyedUnarchiver.unarchiveTopLevelObjectWithData(messageData) as? Joy
                    else {
                        print("Unable to decode Joy object received from watch")
                        return
                }
                
                // Send decoded object to main view controller
                globalJoy = joyObject
            }
            
            catch {
                print("Error unarchiving data: \(error)")
            }
        }
    }
}
