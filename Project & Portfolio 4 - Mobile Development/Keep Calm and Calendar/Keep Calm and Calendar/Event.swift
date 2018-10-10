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
    
    
    
    // MARK: - Computed Properties
    
    
    
    // MARK: - Initializers
    init(name: String, date: Date, image: UIImage, completion: Bool, recurrenceFrequency: Int, originalIndex: Int) {
        self.name = name
        self.date = date
        self.image = image
        self.requiresCompletion = completion
        self.recurrenceFrequency = recurrenceFrequency
        self.originalIndex = originalIndex
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
}
