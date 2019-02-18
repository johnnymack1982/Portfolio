//
//  AppDelegate.swift
//  MackJohn_CrypJoy
//
//  Created by Johnny Mack on 2/14/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import WatchConnectivity

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
        DispatchQueue.main.async {
            // If message is received from watch, continue
            if(message["updateData"] as? Bool) != nil {
                // Reference class to be shared across devices
                NSKeyedArchiver.setClassName("Joy", for: Joy.self)
                
                // Reference main view controller and get current Joy data
                let viewController = ViewController()
                let joy = viewController.joy
                
                // Attempt to encode Joy data and send to watch
                guard let data = try? NSKeyedArchiver.archivedData(withRootObject: joy as Any, requiringSecureCoding: false)
                    else {
                        fatalError("Error")
                }
                
                replyHandler(["updatedData": data])
            }
        }
    }
}
