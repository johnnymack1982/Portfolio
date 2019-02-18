//
//  Joy.swift
//  MackJohn_CrypJoy
//
//  Created by Johnny Mack on 2/17/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

class Joy: NSObject, NSCoding, Codable {
    
    
    
    // MARK: - Class Properties
    var giveGoal: Int?
    var giveProgress: Int?
    var getProgress: Int?
    var payItForwardGoal: Int?
    var payItForwardProgress: Int?
    var dateCreated: Date?
    
    
    
    // MARK: - Default Initializer
    init(giveGoal: Int, giveProgress: Int, getProgress: Int, payItForwardGoal: Int, payItForwardProgress: Int) {
        self.giveGoal = giveGoal
        self.giveProgress = giveProgress
        self.getProgress = getProgress
        self.payItForwardGoal = payItForwardGoal
        self.payItForwardProgress = payItForwardProgress
        dateCreated = NSDate.init(timeIntervalSinceNow: 0) as Date
    }
    
    
    
    // MARK: - Convenience Initializer
    required convenience init?(coder aDecoder: NSCoder) {
        // Set dummy data
        self.init(giveGoal: 1000, giveProgress: 2000, getProgress: 3000, payItForwardGoal: 4000, payItForwardProgress: 5000)
        
        // Decode Joy properties
        giveGoal = aDecoder.decodeObject(forKey: "giveGoalInt") as? Int
        giveProgress = aDecoder.decodeObject(forKey: "giveProgressInt") as? Int
        getProgress = aDecoder.decodeObject(forKey: "getProgressInt") as? Int
        payItForwardGoal = aDecoder.decodeObject(forKey: "payItForwardGoalInt") as? Int
        payItForwardProgress = aDecoder.decodeObject(forKey: "payItForwardProgressInt") as? Int
    }
    
    
    
    // MARK: - Encoder
    func encode(with aCoder: NSCoder) {
        // Encode Joy properties
        aCoder.encode(giveGoal, forKey: "giveGoalInt")
        aCoder.encode(giveProgress, forKey: "giveProgressInt")
        aCoder.encode(getProgress, forKey: "getProgressInt")
        aCoder.encode(payItForwardGoal, forKey: "payItForwardGoalInt")
        aCoder.encode(payItForwardProgress, forKey: "payItForwardProgressInt")
    }
    
    
    
    // MARK: - Setters
    func setGiveGoal(newValue: Int) {
        giveGoal = newValue
    }
    
    func setGiveProgress(newValue: Int) {
        giveProgress = newValue
    }
    
    func setGetProgress(newValue: Int) {
        getProgress = newValue
    }
    
    func setPayItForwardGoal(newValue: Int) {
        payItForwardGoal = newValue
    }
    
    func setPayItForwardProgress(newValue: Int) {
        payItForwardProgress = newValue
    }
    
    
    
    // MARK: - Getters
    func readGiveGoal() -> Int {
        return giveGoal!
    }
    
    func readGiveProgress() -> Int {
        return giveProgress!
    }
    
    func readGetProgress() -> Int {
        return getProgress!
    }
    
    func readPayItForwardGoal() -> Int {
        return payItForwardGoal!
    }
    
    func readPayItForwardProgress() -> Int {
        return payItForwardProgress!
    }
    
    func readDateStamp() -> Date {
        return dateCreated!
    }
    
    
    
    // MARK: - Custom Functions
    public func joyGiven() {
        giveProgress! += 1
        
        if payItForwardProgress! + 1 <= payItForwardGoal! {
            payItForwardProgress! += 1
        }
    }
    
    public func joyReceived() {
        getProgress! += 1
        payItForwardGoal! += 1
        giveGoal! = 3 + payItForwardGoal!
    }
    
    public func displayGiven() -> String {
        let goalString = String(giveGoal!)
        let progressString = String(giveProgress!)
        
        return "\(progressString)/\(goalString)"
    }
    
    public func displayReceived() -> String {
        let progressString = String(getProgress!)
        
        return progressString
    }
    
    public func displayPayItForward() -> String {
        let goalString = String(payItForwardGoal!)
        let progressString = String(payItForwardProgress!)
        
        return "\(progressString)/\(goalString)"
    }
}
