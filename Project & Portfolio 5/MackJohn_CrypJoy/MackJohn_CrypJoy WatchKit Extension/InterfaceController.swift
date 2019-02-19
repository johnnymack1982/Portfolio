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
        
        // If received data is a valid Joy object, set local data to match
        if let context: Joy = context as? Joy {
            self.joy = context
            globalJoy = self.joy
        }
        
        // If global Joy object holds a value set local variable to match
        if let globalJoy = globalJoy {
            joy = globalJoy
        }
        
        // If no data currently exists, call custom method to fetch saved data
        else {
            getData()
        }
        
        // Call custom method to update UI with current values
        updateDisplay()
        
        // Call custom method to check the users progress and enable/disable buttons as necessary
        checkProgress()
        
        // Clear top navigation item
        setTitle("")
    }
    
    override func willActivate() {
        // This method is called when watch view controller is about to be visible to user
        super.willActivate()
    }
    
    override func willDisappear() {
        // Make sure global Joy object matches current local object
        // This is used to make sure data persists when user swipes to the Get Joy screen
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
        // Send current Joy object to next interface controller
        return self.joy
    }
    
    
    
    // MARK: - Custom Functions
    // Custom function to load saved data
    func getData() {
        // Reference save file path
        let filename = getDocumentsDirectory().appendingPathComponent("joy.json")
        
        do {
            // Load raw data from file
            let jsonString = try String(contentsOf: filename)
            
            // Attempt to convert to readable JSON
            if let jsonData = jsonString.data(using: .utf8)
            {
                // Convert JSON to readable Joy object
                let joyObject = try? JSONDecoder().decode(Joy.self, from: jsonData)
                
                // Extract current date from system
                let currentDate = NSDate.init(timeIntervalSinceNow: 0) as Date
                let calendar = Calendar.current
                
                // Create date components from Joy object datestamp and current date to compare
                // This will be used to determine if the loaded data is relevant to the current day
                let createdComponents = calendar.dateComponents([.year, .month, .day], from: (joyObject?.readDateStamp())!)
                let currentComponents = calendar.dateComponents([.year, .month, .day], from: (currentDate))
                
                // If the loaded data is relevant to the current day, use it for the remainder of the session
                if createdComponents.day == currentComponents.day {
                    
                    // USE THIS LINE TO RESET JOY OBJECT DURING TESTING
                    // WHEN NOT IN USE, THIS LINE SHOULD BE COMMENTED OUT
//                    joyObject = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                    
                    joy = joyObject
                }
                
                // If the current date is not relevant to the current day, start fresh
                else {
                    joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                }
                
                // Set global Joy object to match class property
                globalJoy = joy
                
                // Call custom method to update UI with current values
                updateDisplay()
            }
        }
        
        // If an error occurs, log and create a new Joy object to work with
        catch {
            print("Error reading data from file")
            
            joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
            
            globalJoy = joy
            updateDisplay()
        }
    }
    
    // Custom method to update UI with current values
    func updateDisplay() {
        if let joy = joy {
            // Display current values
            giveJoyDisplay.setTitle(joy.displayGiven())
            
            // Call custom function to save current values to file
            saveData()
            
            // Call custom function to send current values to iOS portion
            sendData()
        }
    }
    
    // Custom function to save current values to file
    func saveData() {
        // Encode current values to JSON
        let encodedObject = try? JSONEncoder().encode(joy)
        
        // Attempt to convert JSON to JSON string
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            print(encodedObjectJsonString)
            
            // Reference file path
            let fileName = getDocumentsDirectory().appendingPathComponent("joy.json")
            
            do {
                // Attempt to save JSON string to file
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
            
            catch {
                print("Failed to write data to file")
            }
        }
    }
    
    // Custom function to send current values to iOS portion
    func sendData() {
        DispatchQueue.main.async {
            // Set class for encoding
            NSKeyedArchiver.setClassName("Joy", for: Joy.self)
            
            // Attempt to encode current Joy object
            guard let data = try? NSKeyedArchiver.archivedData(withRootObject: self.joy!, requiringSecureCoding: false)
                else {
                    print("Error encoding Joy data")
                    return
            }
            
            // Attempt to send encoded data to iOS portion
            self.session?.sendMessageData(data, replyHandler: nil, errorHandler: { (error) in
                print("Error sending data to phone: \(error)")
            })
        }
    }
    
    // Custom helper function to build file path for saving and loading
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    // Custom function to check the users progress and enable/disable buttons as necessary
    func checkProgress() {
        // Extract Give Joy goal and progress
        let giveProgress = joy!.readGiveProgress()
        let giveGoal = joy!.readGiveGoal()
        
        // If the current progress is less than the goal, enable button to allow user to log a new act of kindess
        if giveProgress < giveGoal && giveProgress < 9 {
            giveJoyDisplay.setEnabled(true)
        }
        
        // If the current progress is NOT less than the goal, disable the button
        else {
            giveJoyDisplay.setEnabled(false)
        }
    }
}
