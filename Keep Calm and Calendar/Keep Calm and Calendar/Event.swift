//
//  Event.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation
import UIKit



class Event {
    // MARK: - Stored Properties
    var name: String
    var date: Date
    var image: UIImage
    var requiresCompletion: Bool
    var recurrenceFrequency: Int
    var originalIndex: Int
    var isComplete: Bool
    
    
    
    // MARK: - Computed Properties
    
    
    
    // MARK: - Initializers
    init(name: String, date: Date, image: UIImage, requiresCompletion: Bool, recurrenceFrequency: Int, originalIndex: Int, isComplete: Bool) {
        self.name = name
        self.date = date
        self.image = image
        self.requiresCompletion = requiresCompletion
        self.recurrenceFrequency = recurrenceFrequency
        self.originalIndex = originalIndex
        self.isComplete = isComplete
    }
    
    
    
    // MARK: - Custom Functions
    func month() -> Int {
        let calendar = Calendar.current
        return calendar.component(.month, from: date)
    }
    
    func day() -> Int {
        let calendar = Calendar.current
        return calendar.component(.day, from: date)
    }
    
    func year() -> Int {
        let calendar = Calendar.current
        return calendar.component(.year, from: date)
    }
    
    func hour() -> Int {
        let calendar = Calendar.current
        return calendar.component(.hour, from: date)
    }
    
    func minute() -> Int {
        let calendar = Calendar.current
        return calendar.component(.minute, from: date)
    }
    
    func time() -> String {
        var returnString: String = ""
        
        let calendar = Calendar.current
        let hour = calendar.component(.hour, from: date)
        let minute = calendar.component(.minute, from: date)
        
        if hour > 12 {
            returnString = "\(hour - 12):\(String(format: "%02d", minute)) PM"
        }
        
        else {
            returnString = "\(hour):\(String(format: "%02d", minute)) AM"
        }
        
        return returnString
    }
    
    func weekDay() -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "EEEE"
        return formatter.string(from: date)
    }
    
    func filterDate() -> (month: Int, day: Int, year: Int) {
        let month = self.month()
        let day = self.day()
        let year = self.year()
        
        return (month, day, year)
    }
    
    func monthName() -> String {
        var returnString = ""
        
        switch month() {
        case 1:
            returnString = "Jan."
            
        case 2:
            returnString = "Feb."
            
        case 3:
            returnString = "Mar."
            
        case 4:
            returnString = "Apr."
            
        case 5:
            returnString = "May"
            
        case 6:
            returnString = "Jun."
            
        case 7:
            returnString = "Jul."
            
        case 8:
            returnString = "Aug."
            
        case 9:
            returnString = "Sep."
            
        case 10:
            returnString = "Oct."
            
        case 11:
            returnString = "Nov."
            
        case 12:
            returnString = "Dec."
            
        default:
            print("Invalid Month")
        }
        
        return returnString
    }
}
