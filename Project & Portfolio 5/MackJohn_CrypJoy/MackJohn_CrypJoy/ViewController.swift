//
//  ViewController.swift
//  MackJohn_CrypJoy
//
//  Created by Johnny Mack on 2/14/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import UIKit
import SpriteKit

class ViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var giveJoyDisplay: UILabel!
    @IBOutlet weak var getJoyDisplay: UILabel!
    @IBOutlet weak var payItForwardDisplay: UILabel!
    @IBOutlet weak var giveJoyIndicator: SKView!
    @IBOutlet weak var payItForwardIndicator: SKView!
    
    
    
    // MARK: - Class Properties
    public var joy: Joy?
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        // This line is for testing purposes only and should be commented out during normal use
//        testDisplay()
        
        if viewController == nil {
            viewController = self
        }
        
        getData()
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
        
        setupGiveRing()
        setupPayItForwardRing()
    }
    
    // Custom function to load saved data
    // Custom function to load saved data
    public func getData() {
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
                    //                    let joyObject = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                    
                    joy = joyObject
                }
                    
                    // If the current date is not relevant to the current day, start fresh
                else {
                    joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
                }
                
                // Call custom method to update UI with current values
                updateDisplay()
            }
        }
            
            // If an error occurs, log and create a new Joy object to work with
        catch {
            print("Error reading data from file")
            
            joy = Joy(giveGoal: 3, giveProgress: 0, getProgress: 0, payItForwardGoal: 0, payItForwardProgress: 0)
            
            updateDisplay()
        }
    }
    
    // Custom helper function to build file path for saving and loading
    func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    // Custom function to display progress ring
    func setupGiveRing() {
        let scene = SKScene(size: CGSize(width: 145, height: 145))
        scene.scaleMode = .aspectFit
        scene.backgroundColor = .clear
        
        var fraction: CGFloat = 0
        
        if joy != nil {
            let giveProgress = CGFloat((joy?.giveProgress)!)
            let giveGoal = CGFloat((joy?.giveGoal)!)
            
            let displayValue = giveProgress / giveGoal
            
            fraction = displayValue
        }
        
        let path = UIBezierPath(arcCenter: .zero, radius: 50, startAngle: 0, endAngle: 2 * .pi * fraction, clockwise: true)
        let shapeNode = SKShapeNode(path: path.cgPath)
        shapeNode.strokeColor = hexStringToUIColor(hex: "#FFBF46")
        shapeNode.fillColor = .clear
        shapeNode.lineWidth = 7
        shapeNode.lineCap = .round
        shapeNode.position = CGPoint(x: scene.size.width / 2, y: scene.size.height / 2)
        scene.addChild(shapeNode)
        
        giveJoyIndicator.allowsTransparency = true
        giveJoyIndicator.presentScene(scene)
    }
    
    func setupPayItForwardRing() {
        let scene = SKScene(size: CGSize(width: 145, height: 145))
        scene.scaleMode = .aspectFit
        scene.backgroundColor = .clear
        
        var fraction: CGFloat = 0
        
        if joy != nil {
            let giveProgress = CGFloat((joy?.payItForwardProgress)!)
            let giveGoal = CGFloat((joy?.payItForwardGoal)!)
            
            let displayValue = giveProgress / giveGoal
            
            fraction = displayValue
        }
        
        let path = UIBezierPath(arcCenter: .zero, radius: 50, startAngle: 0, endAngle: 2 * .pi * fraction, clockwise: true)
        let shapeNode = SKShapeNode(path: path.cgPath)
        shapeNode.strokeColor = hexStringToUIColor(hex: "#FFBF46")
        shapeNode.fillColor = .clear
        shapeNode.lineWidth = 7
        shapeNode.lineCap = .round
        shapeNode.position = CGPoint(x: scene.size.width / 2, y: scene.size.height / 2)
        scene.addChild(shapeNode)
        
        payItForwardIndicator.allowsTransparency = true
        payItForwardIndicator.presentScene(scene)
    }
    
    func hexStringToUIColor (hex:String) -> UIColor {
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.count) != 6) {
            return UIColor.gray
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
