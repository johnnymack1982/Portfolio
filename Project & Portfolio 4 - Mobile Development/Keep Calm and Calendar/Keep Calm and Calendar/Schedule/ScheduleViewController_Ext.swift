//
//  ScheduleViewController_Ext.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation
import UIKit

extension ScheduleViewController {
    func filterEvents() {
        extractDates()
        
        var currentIndex = 0
        
        for date in dates {
            if filteredEvents.count == 0 {
                filteredEvents.append([])
            }
                
            else if filteredEvents.count != currentIndex + 1 {
                filteredEvents.append([])
            }
            
            filteredEvents[currentIndex] = testEvents.filter({ $0.filterDate() == date })
            
            currentIndex += 1
        }
    }
    
    func extractDates() {
        for event in testEvents {
            let extractedDate: (month: Int, day: Int, year: Int) = (event.month(), event.day(), event.year())
            
            dates.append(extractedDate)
        }
        
        var currentIndex = 0
        
        for date in dates {
            if currentIndex != 0 {
                if date.month == dates[currentIndex - 1].month && date.day == dates[currentIndex - 1].day && date.year == dates[currentIndex - 1].year {
                    dates.remove(at: currentIndex)
                    currentIndex -= 1
                }
            }
            
            currentIndex += 1
        }
    }
}
