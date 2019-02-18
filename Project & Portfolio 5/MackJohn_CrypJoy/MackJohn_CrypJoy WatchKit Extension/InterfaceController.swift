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
        
        else {
            getData()
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
    }
    
    override func contextForSegue(withIdentifier segueIdentifier: String) -> Any? {
        return self.joy
    }
    
    
    
    // MARK: - Custom Functions
    func getData() {
        let filename = getDocumentsDirectory().appendingPathComponent("joy.json")
        do {
            let jsonString = try String(contentsOf: filename)
            
            if let jsonData = jsonString.data(using: .utf8)
            {
                let joyObject = try? JSONDecoder().decode(Joy.self, from: jsonData)
                let currentDate = NSDate.init(timeIntervalSinceNow: 0) as Date
                let calendar = Calendar.current
                let createdComponents = calendar.dateComponents([.year, .month, .day], from: (joyObject?.readDateStamp())!)
                let currentComponents = calendar.dateComponents([.year, .month, .day], from: (currentDate))
                
                if createdComponents.day == currentComponents.day {
                    joy = joyObject
                }
                
                else {
                    joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                }
                
                globalJoy = joy
                updateDisplay()
            }
        }
        
        catch {
            print("Error reading data from file")
            
            joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
            
            globalJoy = joy
            updateDisplay()
        }
    }
    
    func updateDisplay() {
        if let joy = joy {
            giveJoyDisplay.setTitle(joy.displayGiven())
            saveData()
        }
    }
    
    func saveData() {
        let encodedObject = try? JSONEncoder().encode(joy)
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            print(encodedObjectJsonString)
            
            let fileName = getDocumentsDirectory().appendingPathComponent("joy.json")
            
            do {
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
            
            catch {
                print("Failed to write data to file")
            }
        }
    }
    
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
}
