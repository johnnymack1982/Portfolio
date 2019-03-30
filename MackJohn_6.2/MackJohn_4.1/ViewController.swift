//(
//  ViewController.swift
//  MackJohn_4.1
//
//  Created by Johnny Mack on 9/1/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

/*
 ATTRIBUTIONS:
 carpuzi. (2017, February 28). Background Music Treatment. Retreived from https://freesound.org/people/carpuzi/sounds/382327/
 craigmalony. (2016, December 18). countdown.wav. Retreived from https://freesound.org/people/craigmaloney/sounds/371180/
 f4ngy. (2014, June 17). Card Flip. Retreived from https://freesound.org/people/f4ngy/sounds/240776/
 PlooQ. (2014, July 16). Go_1.wav. Retreived from https://freesound.org/people/PlooQ/sounds/242733/
 InspectorJ. (2017, November 23). Pop, High, A (H1).wav. Retreived from https://freesound.org/people/InspectorJ/sounds/411642/
 LittleRobotSoundFactory. (2015, May 15). Jingle_Win_Synth_01.wav. Retreived from https://freesound.org/people/LittleRobotSoundFactory/sounds/274179/
 
 M"atch.ly", "Play", "Stop", and and "You Win!" graphics created at: https://cooltext.com
 */

import UIKit
import AVFoundation
import CoreData

class ViewController: UIViewController {
    
    
    
    // MARK: - UI Outlets
    // First Row
    // iPhone Tiles
    @IBOutlet weak var tile0_0: UIImageView!
    @IBOutlet weak var tile0_1: UIImageView!
    @IBOutlet weak var tile0_2: UIImageView!
    @IBOutlet weak var tile0_3: UIImageView!
    
    // iPad Tiles
    @IBOutlet weak var tile0_4_T: UIImageView!
    
    // Second Row
    // iPhone Tiles
    @IBOutlet weak var tile1_0: UIImageView!
    @IBOutlet weak var tile1_1: UIImageView!
    @IBOutlet weak var tile1_2: UIImageView!
    @IBOutlet weak var tile1_3: UIImageView!
    
    // iPad tiles
    @IBOutlet weak var tile1_4_T: UIImageView!
    
    // Third Row
    // iPhone Tiles
    @IBOutlet weak var tile2_0: UIImageView!
    @IBOutlet weak var tile2_1: UIImageView!
    @IBOutlet weak var tile2_2: UIImageView!
    @IBOutlet weak var tile2_3: UIImageView!
    
    // iPad Tiles
    @IBOutlet weak var tile2_4_T: UIImageView!
    
    // Fourth Row
    // iPhone Tiles
    @IBOutlet weak var tile3_0: UIImageView!
    @IBOutlet weak var tile3_1: UIImageView!
    @IBOutlet weak var tile3_2: UIImageView!
    @IBOutlet weak var tile3_3: UIImageView!
    
    // iPad Tiles
    @IBOutlet weak var tile3_4_T: UIImageView!
    
    // Fifth Row
    // iPhone Tiles
    @IBOutlet weak var tile4_0: UIImageView!
    @IBOutlet weak var tile4_1: UIImageView!
    @IBOutlet weak var tile4_2: UIImageView!
    @IBOutlet weak var tile4_3: UIImageView!
    
    // iPad Tiles
    @IBOutlet weak var tile4_4_T: UIImageView!
    
    // Sixth Row
    // iPad tiles
    @IBOutlet weak var tile5_0_T: UIImageView!
    @IBOutlet weak var tile5_1_T: UIImageView!
    @IBOutlet weak var tile5_2_T: UIImageView!
    @IBOutlet weak var tile5_3_T: UIImageView!
    @IBOutlet weak var tile5_4_T: UIImageView!
    
    // Timer Labels
    @IBOutlet weak var portraitTimer: UILabel!
    @IBOutlet weak var landscapeTimer: UILabel!
    @IBOutlet weak var landscapeFinalTimeLabel: UILabel!
    @IBOutlet weak var finalTimeLabel: UILabel!
    
    // Graphics
    @IBOutlet weak var playButton: UIImageView!
    @IBOutlet weak var youWinGraphic: UIImageView!
    
    
    
    // MARK: - Class Properties
    // iPhone tiles
    // These will be used throughout the app, regardless of class size
    var iPhoneTiles: [UIImageView] = []
    var iPhoneTileImages: [UIImage] = [#imageLiteral(resourceName: "Banana"), #imageLiteral(resourceName: "Banana"), #imageLiteral(resourceName: "Brain"), #imageLiteral(resourceName: "Brain"), #imageLiteral(resourceName: "Brush"), #imageLiteral(resourceName: "Brush"), #imageLiteral(resourceName: "Cauldron"), #imageLiteral(resourceName: "Cauldron"), #imageLiteral(resourceName: "Chest"), #imageLiteral(resourceName: "Chest"), #imageLiteral(resourceName: "Construction"), #imageLiteral(resourceName: "Construction"), #imageLiteral(resourceName: "Corn"), #imageLiteral(resourceName: "Corn"), #imageLiteral(resourceName: "Gold"), #imageLiteral(resourceName: "Gold"), #imageLiteral(resourceName: "Hat"), #imageLiteral(resourceName: "Hat"), #imageLiteral(resourceName: "Heart"), #imageLiteral(resourceName: "Heart")]
    
    // iPad tiles
    // These will be added to iPhoneTiles ONLY when the game is played on an iPad
    var iPadTiles: [UIImageView] = []
    var iPadTileImages: [UIImage] = [#imageLiteral(resourceName: "Mushroom"), #imageLiteral(resourceName: "Mushroom"), #imageLiteral(resourceName: "Potion"), #imageLiteral(resourceName: "Potion"), #imageLiteral(resourceName: "Radio"), #imageLiteral(resourceName: "Radio"), #imageLiteral(resourceName: "Rock"), #imageLiteral(resourceName: "Rock"), #imageLiteral(resourceName: "Smiley"), #imageLiteral(resourceName: "Smiley")]
    
    // Tracks the user's selections, two tiles at a time
    var selectedTiles: [UIImageView] = []
    
    // Default tile count for iPhone is 20
    // This will be updated if the game is played on an iPad
    var tileCount = 20
    
    // Countdown Timer
    var seconds = 5
    var countDownTimer = Timer()
    var isCountDownActive = false
    
    // Play Timer
    var startTime = TimeInterval()
    var playTimer = Timer()
    
    // Colors
    var defaultTileColor: CGColor?
    
    // Indicators
    var isGameRunning = false
    var iPadActive = false
    
    // Sound Effects
    var backgroundMusicPlayer = AVAudioPlayer()
    var countdownPlayer = AVAudioPlayer()
    var goPlayer = AVAudioPlayer()
    var flipPlayer = AVAudioPlayer()
    var popPlayer = AVAudioPlayer()
    var winPlayer = AVAudioPlayer()
    var backgroundPlayer = AVAudioPlayer()
    
    // Leaderboard Information
    var playerName = ""
    var completionTime: (minutes: Int, seconds: Int, miliseconds: Int) = (0, 0, 0)
    var totalMoves = 0
    var dateStamp = Date()
    var leaderBoard: [(playerName: String, completionTime: (minutes: Int, seconds: Int, miliseconds: Int), totalMoves: Int, dateStamp: Date)] = []
    
    // Core Data Objects
    var managedContext: NSManagedObjectContext!
    var entityDescription: NSEntityDescription!
    var savedScore: NSManagedObject!
    
    
    
    // Mark: - System Generated Functions
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set up core data context and entity description
        managedContext = (UIApplication.shared.delegate as? AppDelegate)?.managedObjectContext
        entityDescription = NSEntityDescription.entity(forEntityName: "Leaderboard", in: managedContext)
        
        // Create fetch request to load leaderboard data
        let fetchRequest = NSFetchRequest<NSManagedObject>(entityName: "Leaderboard")
        
        // Attempt to load leaderboard data
        do {
            let results: [NSManagedObject] = try managedContext.fetch(fetchRequest)
            
            // Clear loacal leaderboard data
            leaderBoard = []
            
            // Loop through loaded leaderboard data
            for score in results {
                savedScore = score
                
                // If the current object isn't nil, load it into memory
                if savedScore != nil {
                    var scoretoLoad: (playerName: String, completionTime: (minutes: Int, seconds: Int, miliseconds: Int), totalMoves: Int, dateStamp: Date) = ("", (0, 0, 0), 0, Date())
                    
                    // Reference the player name, completion time, total moves, and date stamp for the current high score
                    scoretoLoad.playerName = (savedScore.value(forKey: "playerName") as? String)!
                    scoretoLoad.completionTime.minutes = (savedScore.value(forKey: "playerMinutes") as? Int)!
                    scoretoLoad.completionTime.seconds = (savedScore.value(forKey: "playerSeconds") as? Int)!
                    scoretoLoad.completionTime.miliseconds = (savedScore.value(forKey: "playerMiliseconds") as? Int)!
                    scoretoLoad.totalMoves = (savedScore.value(forKey: "playerMoves") as? Int)!
                    scoretoLoad.dateStamp = (savedScore.value(forKey: "playerDate") as? Date)!
                    
                    // Add the loaded score information to local memory
                    leaderBoard.append(scoretoLoad)
                }
            }
        }
        
        catch {
            print("Error loading leaderboard")
        }
        
        // Set default tile color for when a tile is "face down"
        defaultTileColor = tile0_0.layer.backgroundColor
        
        // Hide timers until game starts
        portraitTimer.isHidden = true
        landscapeTimer.isHidden = true
        
        // Hide Win indicators until game is over
        finalTimeLabel.isHidden = true
        landscapeFinalTimeLabel.isHidden = true
        youWinGraphic.isHidden = true
        
        // Reference tiles to appear on iPhone
        iPhoneTiles = [tile0_0, tile0_1, tile0_2, tile0_3, tile1_0, tile1_1, tile1_2, tile1_3, tile2_0, tile2_1, tile2_2, tile2_3, tile3_0, tile3_1, tile3_2, tile3_3, tile4_0, tile4_1, tile4_2, tile4_3]
        
        // Reference additional tiles to appear on iPad
        iPadTiles = [tile0_4_T, tile1_4_T, tile2_4_T, tile3_4_T, tile4_4_T, tile5_0_T, tile5_1_T, tile5_2_T, tile5_3_T, tile5_4_T]
        
        // Call custom function to check the class size for the current device
        phoneOrTablet()
        
        // Round corners for all tiles
        for tile in iPhoneTiles {
            tile.layer.cornerRadius = 10
            tile.isHidden = true
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    // MARK: - Action Functions
    // Action to execute when Play/Stop button is tapped
    @IBAction func playButtonTapped(recognizer: UITapGestureRecognizer) {
        
        // If the game isn't running, this will run
        if isGameRunning == false {
            
            // Create player name input dialogue
            let playerNameInput = UIAlertController(title: "Let's Play!", message: "Please enter your INITIALS:", preferredStyle: .alert)
            
            // Add text field to the input dialogue
            playerNameInput.addTextField { (nameInput) in
                nameInput.placeholder = "Player Initials"
            }
            
            // Create OK button for player name input and add to the dialogue
            let okButton = UIAlertAction(title: "OK", style: .default) { (action) in
                if playerNameInput.textFields![0].text != "" && (playerNameInput.textFields![0].text?.count)! <= 3 {
                    self.playerName = playerNameInput.textFields![0].text!.uppercased()
                }
                
                else {
                    self.playerName = "ANM"
                }
                
                // Call custom function to continue after player enters their name
                self.proceed()
            }
            
            playerNameInput.addAction(okButton)
            
            // Create Cancel button for player name input and add to the dialogue
            let cancelButton = UIAlertAction(title: "Cancel", style: .cancel) { (action) in
            }
            
            playerNameInput.addAction(cancelButton)
            
            self.present(playerNameInput, animated: true, completion: nil)
        }
            
        // If the game is running, this will activate
        else {
            
            // Stop background music
            backgroundPlayer.stop()
            
            // Hide all play tiles
            for tile in iPhoneTiles {
                tile.isHidden = true
            }
            
            // Hide timers
            portraitTimer.isHidden = true
            landscapeTimer.isHidden = true
            
            // Set Play/Stop button to Play
            playButton.image = #imageLiteral(resourceName: "Play")
            
            // Stop and reset play timer
            playTimer.invalidate()
            startTime = TimeInterval()
            playTimer = Timer()
            
            // Indicate that the game is not running
            isGameRunning = false
        }
    }
    
    // Action to fire when a tile is tapped
    @IBAction func tileTapped(recognizer: UITapGestureRecognizer) {        
        // Reference the index for the selected tile
        if let tile = recognizer.view as? UIImageView {
            // Prevent user from selecting tile twice
            tile.isUserInteractionEnabled = false
            
            // Call custom function to "flip" tile and reveal its image
            faceUp(tile: tile)
            
            // Track selected tiles
            selectedTiles.append(tile)
            
            totalMoves += 1
            
            print(totalMoves.description)
        }
        
        // If the user has chosen two tiles, see if they're a match
        if selectedTiles.count == 2 {
            
            // Prevent user from selecting any other tiles for a moment
            for tile in iPhoneTiles {
                tile.isUserInteractionEnabled = false
            }
            
            // Call custom function to trigger a short delay and see if the tiles are a match
            delayTimer()
        }
    }
    
    // Send current leaderboard information to leaderboard view controller
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let destination = segue.destination as? LeaderboardViewController {
            destination.leaderBoard = leaderBoard
        }
    }
}

