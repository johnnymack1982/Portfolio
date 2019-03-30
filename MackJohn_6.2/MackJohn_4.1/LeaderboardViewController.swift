//
//  LeaderboardViewController.swift
//  MackJohn_4.1
//
//  Created by Johnny Mack on 9/20/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import UIKit

class LeaderboardViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    // Player Names
    @IBOutlet weak var playerOneName: UILabel!
    @IBOutlet weak var playerTwoName: UILabel!
    @IBOutlet weak var playerThreeName: UILabel!
    @IBOutlet weak var playerFourName: UILabel!
    @IBOutlet weak var playerFiveName: UILabel!
    
    // Player Times
    @IBOutlet weak var playerOneTime: UILabel!
    @IBOutlet weak var playerTwoTime: UILabel!
    @IBOutlet weak var playerThreeTime: UILabel!
    @IBOutlet weak var playerFourTime: UILabel!
    @IBOutlet weak var playerFiveTime: UILabel!
    
    // Player Moves
    @IBOutlet weak var playerOneMoves: UILabel!
    @IBOutlet weak var playerTwoMoves: UILabel!
    @IBOutlet weak var playerThreeMoves: UILabel!
    @IBOutlet weak var playerFourMoves: UILabel!
    @IBOutlet weak var playerFiveMoves: UILabel!
    
    // Player Dates
    @IBOutlet weak var playerOneDate: UILabel!
    @IBOutlet weak var playerTwoDate: UILabel!
    @IBOutlet weak var playerThreeDate: UILabel!
    @IBOutlet weak var playerFourDate: UILabel!
    @IBOutlet weak var playerFiveDate: UILabel!
    
    
    
    // MARK: Class Properties
    var leaderBoard: [(playerName: String, completionTime: (minutes: Int, seconds: Int, miliseconds: Int), totalMoves: Int, dateStamp: Date)] = []
    var leaderBoardDisplay: [(playerName: UILabel, playerTime: UILabel, playerMoves: UILabel, playerDate: UILabel)] = []
    
    
    
    
    // MARK: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Reference UI outlets in an array of tuples for easier reference later
        leaderBoardDisplay = [(playerOneName, playerOneTime, playerOneMoves, playerOneDate), (playerTwoName, playerTwoTime, playerTwoMoves, playerTwoDate), (playerThreeName, playerThreeTime, playerThreeMoves, playerThreeDate), (playerFourName, playerFourTime, playerFourMoves, playerFourDate), (playerFiveName, playerFiveTime, playerFiveMoves, playerFiveDate)]
        
        // Loop through the UI and clear all score displays
        for score in leaderBoardDisplay {
            score.playerName.text = ""
            score.playerTime.text = ""
            score.playerMoves.text = ""
            score.playerDate.text = ""
        }
        
        // Track current index
        var currentIndex = 0
        
        // Loop through UI display and populate with current leaderboard information
        for score in leaderBoardDisplay {
            
            // Stop looping once the end of the leaderboard array has been reached
            if currentIndex != leaderBoard.count {
                
                // Set current player name, completion time, and total moves
                score.playerName.text = leaderBoard[currentIndex].playerName
                score.playerTime.text = leaderBoard[currentIndex].completionTime.minutes.description + ":" + leaderBoard[currentIndex].completionTime.seconds.description + ":" + leaderBoard[currentIndex].completionTime.miliseconds.description
                score.playerMoves.text = leaderBoard[currentIndex].totalMoves.description
                
                // Properly format and display the date stamp for the current score
                let dateFormatter = DateFormatter()
                dateFormatter.dateStyle = .medium
                dateFormatter.timeStyle = .none
                dateFormatter.locale = Locale(identifier: "en_US")
                score.playerDate.text = dateFormatter.string(from: leaderBoard[currentIndex].dateStamp)
                
                // Increase current index by one
                currentIndex += 1
            }
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    // MARK: - Action Functions
    @IBAction func backTapped(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
    }
}
