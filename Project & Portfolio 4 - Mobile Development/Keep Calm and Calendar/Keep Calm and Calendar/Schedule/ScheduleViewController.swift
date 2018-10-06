//
//  ScheduleViewController.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class ScheduleViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    
    
    // MARK: - UI Outlets
    @IBOutlet weak var tableView: UITableView!
    
    
    
    // MARK: - Class Properties
    var cellCount = 1
    let testEvents: [Event] = [Event(name: "Wake Up", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: true), Event(name: "Breakfast", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: true), Event(name: "School", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: false), Event(name: "Home", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: false), Event(name: "Homework", date: Date(), image: #imageLiteral(resourceName: "Logo"), completion: false)]
    var filteredEvents: [[Event]?] = []
    var dates: [(month: Int, day: Int, year: Int)] = []
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationController?.navigationBar.isHidden = true
        
        // Register xib
        let headerNib = UINib.init(nibName: "WeekHeader", bundle: nil)
        tableView.register(headerNib, forHeaderFooterViewReuseIdentifier: "header_01")
        
        filterEvents()
        
        tableView.reloadData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    // MARK: - Table view data source
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let filteredEvents = filteredEvents[section] {
            return filteredEvents.count
        }
        
        else {
            return 0
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return filteredEvents.count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return nil
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 75
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var returnCell: EventCell?
        
        switch cellCount {
        case 1:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_01", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_01", for: indexPath)
            }
            
            returnCell = cell
            
        case 2:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_02", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_02", for: indexPath)
            }
            
            returnCell = cell
            
        case 3:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_03", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_03", for: indexPath)
            }
            
            returnCell = cell
            
        case 4:
            guard let cell = tableView.dequeueReusableCell(withIdentifier: "cell_04", for: indexPath) as? EventCell
                
                else {
                    return tableView.dequeueReusableCell(withIdentifier: "cell_04", for: indexPath)
            }
            
            returnCell = cell
            
        default:
            print("Invalid")
        }
        
        let currentEvent = filteredEvents[indexPath.section]![indexPath.row]
        
        // Configure the cell...
        returnCell?.eventImage.image = currentEvent.image
        returnCell?.eventNameLabel.text = currentEvent.name
        returnCell?.eventTimeLabel.text = currentEvent.time()
        
        if currentEvent.completion == true {
            returnCell?.taskCompletionIndicator.image = #imageLiteral(resourceName: "Complete")
        }
        
        else {
            returnCell?.taskCompletionIndicator.image = #imageLiteral(resourceName: "Incomplete")
        }
        
        if cellCount == 4 {
            cellCount = 1
        }
        
        else {
            cellCount += 1
        }
        
        return returnCell!
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        var returnHeader: UIView?
        
        let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: "header_01") as? WeeklyHeaderView
        header?.headerTitleLabel.text = filteredEvents[section]?.last?.weekDay()
        
        returnHeader = header
        
        return returnHeader
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func editButtonTapped(_ sender: UIBarButtonItem) {
        let alert = UIAlertController(title: "Coming Soon", message: "Pardon our dust. This feature isn't ready just yet!", preferredStyle: .alert)
        let okButton = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(okButton)
        self.present(alert, animated: true)
    }
}
