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
    let name: String
    let date: Date
    let image: UIImage
    let completion: Bool
    
    
    
    // MARK: - Computed Properties
    
    
    
    // MARK: - Initializers
    init(name: String, date: Date, image: UIImage, completion: Bool) {
        self.name = name
        self.date = date
        self.image = image
        self.completion = completion
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
            returnString = "\(hour - 12):\(minute) PM"
        }
        
        else {
            returnString = "\(hour):\(minute) AM"
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
