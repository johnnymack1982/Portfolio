//
//  ScheduleViewController_Ext.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation
import UIKit
import Firebase
import FirebaseFirestore
import FirebaseStorage

extension ScheduleViewController {
    
    // Custom function to filter events into schedule
    func filterEvents() {
        var currentIndex = 0
        
        for event in events {
            event.originalIndex = currentIndex
            
            currentIndex += 1
        }
        
        // Clear all relevant arrays to prevent duplication
        filteredEvents = []
        tempEvents = []
        dates = []
        dateComponents = []
        
        // Call custom functions to remove old events, generate recurring events, and extract dates needed for filtering
        removeOldEvents()
        
        // Write newly generated data to user's database entry
        let database = Firestore.firestore()
        for event in events {
            database.collection("users").document(self.userEmail!).collection("events").document(event.originalIndex.description).setData([
                "name" : event.name,
                "date" : event.date,
                "imageRef" : (currentUser?.userID)! + "-" + event.originalIndex.description+".jpg",
                "requiresCompletion" : event.requiresCompletion,
                "recurrenceFrequency" : event.recurrenceFrequency,
                "originalIndex" : event.originalIndex,
                "isComplete" : event.isComplete]) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    } else {
                        print("Document successfully written!")
                    }
            }
            
            let storage = Storage.storage()
            let storageRef = storage.reference()
            let imageRef = storageRef.child((currentUser?.userID)! + "-" + event.originalIndex.description+".jpg")
            
            if event.image != #imageLiteral(resourceName: "Logo") {
                let data: Data = UIImageJPEGRepresentation(event.image, 0.25)!
                let uploadTask = imageRef.putData(data)
                uploadTask.resume()
            }
        }
        
        generateRecurrances()
        extractDates()
        
        currentIndex = 0
        
        // Filter events using extracted dates to make sure they are displayed in the correct cells
        for date in dateComponents {

            
            if filteredEvents.count == 0 {
                filteredEvents.append([])
            }
                
            else if filteredEvents.count != currentIndex + 1 {
                filteredEvents.append([])
            }
            
            filteredEvents[currentIndex] = tempEvents.filter({ $0.filterDate() == date })
            
            currentIndex += 1
        }
    }
    
    // Custom function to extract dates for event filtering
    func extractDates() {
        
        // Cycle through events and extract raw date information
        for event in tempEvents {
            dates.append(event.date)
        }
        
        dates = dates.sorted()
        
        var currentIndex = 0
        
        // Cycle through extracted dates and break down into date components
        for date in dates {
            let newDate = Calendar.current.date(bySettingHour: 0, minute: 0, second: 0, of: date)!
            dates[currentIndex] = newDate
            currentIndex += 1
        }
        
        // Remove duplicate dates and sort the remaining
        var tempDates = dates.removingDuplicates()
        tempDates = tempDates.sorted()
        dates = tempDates
        
        // Extract date components from date array
        for date in dates {
            let calendar = Calendar.current
            
            let year = calendar.component(.year, from: date)
            let month = calendar.component(.month, from: date)
            let day = calendar.component(.day, from: date)
            
            dateComponents.append((month, day, year))
        }
    }
    
    // Custom function to generate recurring events
    func generateRecurrances() {
        
        // Will be used to track individual date components
        var dateComponent = DateComponents()
        var daysToAdd = 0
        var weeksToAdd = 0
        var monthsToAdd = 0
        var yearsToAdd = 0
        
        // Cycle through events and generate recurring events based on user preference for each individual item
        for event in events {
            
            // If user selected daily recurrence, generate recurring events 60 days out
            if event.recurrenceFrequency == 1 {
                for _ in 1...60 {
                    dateComponent.day = daysToAdd
                    dateComponent.month = 0
                    dateComponent.year = 0
                    
                    let newDate = Calendar.current.date(byAdding: dateComponent, to: event.date)
                    
                    tempEvents.append(Event(name: event.name, date: newDate!, image: event.image, requiresCompletion: event.requiresCompletion, recurrenceFrequency: event.recurrenceFrequency, originalIndex: event.originalIndex, isComplete: false))
                    
                    daysToAdd += 1
                }
            }
                
                // If user selected weekly recurrince, generate recurring events two weeks out
            else if event.recurrenceFrequency == 2 {
                for _ in 1...8 {
                    dateComponent.day = weeksToAdd
                    dateComponent.month = 0
                    dateComponent.year = 0
                    
                    let newDate = Calendar.current.date(byAdding: dateComponent, to: event.date)
                    
                    tempEvents.append(Event(name: event.name, date: newDate!, image: event.image, requiresCompletion: event.requiresCompletion, recurrenceFrequency: event.recurrenceFrequency, originalIndex: event.originalIndex, isComplete: false))
                    
                    weeksToAdd += 7
                }
            }
                
                // If user selected monthly, generate recurring events two months out
            else if event.recurrenceFrequency == 3 {
                for _ in 1...2 {
                    dateComponent.day = 0
                    dateComponent.month = monthsToAdd
                    dateComponent.year = 0
                    
                    let newDate = Calendar.current.date(byAdding: dateComponent, to: event.date)
                    
                    tempEvents.append(Event(name: event.name, date: newDate!, image: event.image, requiresCompletion: event.requiresCompletion, recurrenceFrequency: event.recurrenceFrequency, originalIndex: event.originalIndex, isComplete: false))
                    
                    monthsToAdd += 1
                }
            }
                
                // If user selected yearly recurrance, generate recurring events two years out
            else if event.recurrenceFrequency == 4 {
                for _ in 1...2 {
                    dateComponent.day = 0
                    dateComponent.month = 0
                    dateComponent.year = yearsToAdd
                    
                    let newDate = Calendar.current.date(byAdding: dateComponent, to: event.date)
                    
                    tempEvents.append(Event(name: event.name, date: newDate!, image: event.image, requiresCompletion: event.requiresCompletion, recurrenceFrequency: event.recurrenceFrequency, originalIndex: event.originalIndex, isComplete: false))
                    
                    yearsToAdd += 1
                }
            }
                
                // If user selected a one time event, do not generate recurring events
            else if event.recurrenceFrequency == 0 {
                tempEvents.append(event)
            }
            
            // Reset date component trackers
            daysToAdd = 0
            weeksToAdd = 0
            monthsToAdd = 0
            yearsToAdd = 0
        }
    }
    
    // Custom function to remove events that have occured in the past
    func removeOldEvents() {
        var currentIndex = 0
        
        // Used solely to compare event date to the current date
        let now = Event(name: "Now", date: Date(), image: #imageLiteral(resourceName: "Logo"), requiresCompletion: false, recurrenceFrequency: 0, originalIndex: 0, isComplete: false)
        
        // Cycle throughh events and remove past items
        for event in events {
            var dateComponent = DateComponents()
            
            // Extract number of days in a given month
            let eventRange = Calendar.current.range(of: .day, in: .month, for: event.date)!
            let nowRange = Calendar.current.range(of: .day, in: .month, for: now.date)
            
            // Reference number of days in a given month
            let eventDays = eventRange.count
            let nowDays = nowRange?.count
            
            // If the current event occurs in a past year...
            if event.year() < now.year() {
                
                // If event is more than a year old, remove it
                if (now.year() - event.year()) > 1 {
                    events.remove(at: currentIndex)
                }
                    
                else {
                    
                    // If event is a one time event, remove it
                    if event.recurrenceFrequency == 0 {
                        events.remove(at: currentIndex)
                    }
                        
                        // If event is daily, remove past events while continuing daily recurrence
                    else if event.recurrenceFrequency == 1 {
                        dateComponent.month = now.month() - event.month()
                        dateComponent.day = now.day() - event.day()
                        dateComponent.year = now.year() - event.year()
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is monthly, remove past events while continuing monthly recurrence
                    else if event.recurrenceFrequency == 3 {
                        dateComponent.month = 1
                        dateComponent.day = 0
                        dateComponent.year = 1
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is yearly, remove past events while continuing yearly recurrence
                    else if event.recurrenceFrequency == 4 {
                        dateComponent.month = 0
                        dateComponent.day = 0
                        dateComponent.year = 1
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                    
                    currentIndex -= 1
                }
            }
                
                // If event takes place in the same year but a past month...
            else if event.month() < now.month() {
                
                // If event is more than a month old, remove it
                if (now.month() - event.month()) > 1 {
                    events.remove(at: currentIndex)
                }
                    
                    // If event is a one time item, remove it
                else {
                    if event.recurrenceFrequency == 0 {
                        events.remove(at: currentIndex)
                    }
                        
                        // If event is daily, remove old events while continuing daily recurrence
                    else if event.recurrenceFrequency == 1 {
                        dateComponent.month = now.month() - event.month()
                        dateComponent.day = now.day() - event.day()
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If devent is weekly, remove old events while continuing weekly recurrence
                    else if event.recurrenceFrequency == 2 {
                        dateComponent.month = now.month() - event.month()
                        
                        if nowDays == 31 && eventDays == 30 {
                            dateComponent.day = -23
                        }
                            
                        else if nowDays == 30 && eventDays == 31 {
                            dateComponent.day = -24
                        }
                            
                        else if nowDays == 30 && eventDays == 30 {
                            dateComponent.day = 0
                        }
                            
                        else if nowDays == 31 && eventDays == 31 {
                            dateComponent.day = -28
                        }
                            
                        else if nowDays == 28 && eventDays == 31 {
                            dateComponent.day = -21
                        }
                            
                        else if nowDays == 31 && eventDays == 28 {
                            dateComponent.day = -21
                        }
                        
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is monthly, remove old events while continuing monthly recurrence
                    else if event.recurrenceFrequency == 3 {
                        dateComponent.day = 0
                        dateComponent.month = 1
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is yearly, remove old events while continueing yearly recurrence
                    else if event.recurrenceFrequency == 4 {
                        dateComponent.day = 0
                        dateComponent.month = 0
                        dateComponent.year = 1
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                    
                    currentIndex -= 1
                }
            }
                
                // If event takes place in the current month and year but is on a past day...
            else if event.day() < now.day() {
                
                // If event is more than a day old, remove it
                if (now.day() - event.day()) > 1 {
                    events.remove(at: currentIndex)
                }
                    
                else {
                    
                    // If event is a one time event, remove it
                    if event.recurrenceFrequency == 0 {
                        events.remove(at: currentIndex)
                    }
                        
                        // If event is daily, remove old events while continuing recurrence
                    else if event.recurrenceFrequency == 1 {
                        dateComponent.month = 0
                        dateComponent.day = now.day() - event.day()
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is weekly, remove old events while continuing recurrence
                    else if event.recurrenceFrequency == 2 {
                        dateComponent.month = 0
                        dateComponent.day = 7
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is monthly, remove old events while continuing recurrence
                    else if event.recurrenceFrequency == 3 {
                        dateComponent.month = 1
                        dateComponent.day = 0
                        dateComponent.year = 0
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                        
                        // If event is yearly, remove old events while continuing recurrence
                    else if event.recurrenceFrequency == 4 {
                        dateComponent.month = 0
                        dateComponent.day = 0
                        dateComponent.year = 1
                        
                        event.date = Calendar.current.date(byAdding: dateComponent, to: event.date)!
                        event.isComplete = false
                    }
                    
                    currentIndex -= 1
                }
            }
            
            currentIndex += 1
        }
    }
    
    // Custom function to toggle Parent Mode on and off
    func toggleParentMode() {
        
        // If Parent Mode is off, prompt for Parent Code
        // Enter Parent Mode upon valid entry of Parent Code
        if parentMode == false {
            let alert = UIAlertController(title: "Parent Code Required", message: "Please enter your 4-digit Parent Code to continue.", preferredStyle: .alert)
            
            let cancelButton = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
            let okButton = UIAlertAction(title: "OK", style: .default) { (parentCode) in
                let parentCodeEntry = alert.textFields![0] as UITextField
                
                if parentCodeEntry.text! == self.parentCode {
                    self.editButton.title = "Back"
                    self.navigationBar.topItem?.title = "Parent Mode"
                    self.parentMode = true
                }
                    
                // If Parent Mode is on, turn it off
                // Does not require Parent Code entry
                else {
                    let alert = UIAlertController(title: "Invalid Code", message: "Oops! The code you entered was not correct.", preferredStyle: .alert)
                    
                    let okButton = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                    alert.addAction(okButton)
                    
                    self.present(alert, animated: true)
                    
                    self.parentMode = false
                }
            }
            
            alert.addAction(cancelButton)
            alert.addAction(okButton)
            alert.addTextField { (textField) in
                textField.placeholder = "Enter Parent Code..."
                textField.textAlignment = .center
            }
            
            self.present(alert, animated: true)
        }
            
        else {
            editButton.title = "Edit"
            navigationBar.topItem?.title = "My Schedule"
            
            parentMode = false
        }
    }
    
    // Custom function to delete selected event while in Parent Mode
    func deleteSelectedEvent() {
        deleteCloudEvents()
        
        events.remove(at: (selectedEvent?.originalIndex)!)
        filterEvents()
        UIView.transition(with: tableView, duration: 1.0, options: .transitionCrossDissolve, animations: {self.tableView.reloadData()}, completion: nil)
    }
    
    // Custom function to clear all cloud-stored events to prevent duplication while application is running
    func deleteCloudEvents() {
        let database = Firestore.firestore()
        
        for currentIndex in 0...self.events.count {
            database.collection("users").document(self.userEmail!).collection("events").document(currentIndex.description).delete() { err in
                if let err = err {
                    print("Error deleting document: \(err)")
                } else {
                    print("Document successfully deleted!")
                }
            }
        }
    }
}
