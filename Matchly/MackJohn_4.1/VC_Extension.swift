//
//  VC_Extension.swift
//  MackJohn_4.1
//
//  Created by Johnny Mack on 9/2/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation
import UIKit
import AVFoundation
import CoreData

extension ViewController {
    
    // Custom function to return class size for current device
    func sizeClass() -> (horizontal: UIUserInterfaceSizeClass, vertical: UIUserInterfaceSizeClass) {
        print("Horizontal: \(self.traitCollection.horizontalSizeClass.rawValue)")
        print("Vertical: \(self.traitCollection.verticalSizeClass.rawValue)")
        
        return (self.traitCollection.horizontalSizeClass, self.traitCollection.verticalSizeClass)
    }
    
    // Custom function to see if current device is an iPad
    func phoneOrTablet() {
        
        // If current device is an iPad, add additional tiles to the game
        if sizeClass().horizontal.rawValue == 2 && sizeClass().horizontal.rawValue == 2 {
            if sizeClass().vertical.rawValue != 1 {
                if iPadActive == false {
                    iPhoneTiles += iPadTiles
                    iPhoneTileImages += iPadTileImages
                    iPadActive = true
                }
                
                tileCount = 30
            }
        }
            
            // Otherwise, make sure there are only enough tiles for iPhone devices
        else {
            tileCount = 20
        }
    }
    
    // Custom function to count down from five and start game
    func countDown() {
        
        // Set countdown to five seconds
        seconds = 5
        
        // Start timer and start game once it reaches zero
        countDownTimer = Timer.scheduledTimer(timeInterval: 1, target: self,   selector: (#selector(ViewController.beginGame)), userInfo: nil, repeats: true)
    }
    
    // Custom function to create short delay while checking to see if selected tiles are a match
    func delayTimer() {
        // Set timer to one second
        seconds = 1
        
        // Timer will delay for 1/3 second and then resume game
        countDownTimer = Timer.scheduledTimer(timeInterval: 0.3, target: self, selector: (#selector(ViewController.resumeGame)), userInfo: nil, repeats: true)
    }
    
    // Custom function to create short delay before showing winning indicators
    func winDelay() {
        // Set timer to one second
        seconds = 1
        
        // Timer will delay for 1/3 second and then resume game
        countDownTimer = Timer.scheduledTimer(timeInterval: 0.3, target: self, selector: (#selector(ViewController.winGame)), userInfo: nil, repeats: true)
    }
    
    // Custom function to how long it takes user to win the game
    @objc func gameTimer() {
        let currentTime = Date.timeIntervalSinceReferenceDate
        
        // Find the difference between current time and start time
        var elapsedTime = currentTime - startTime
        
        // Calculate minutes elapsed
        let minutes = UInt8(elapsedTime / 60.0)
        elapsedTime -= (TimeInterval(minutes) * 60)
        
        // Calculate seconds elapsed
        let seconds = UInt8(elapsedTime)
        elapsedTime -= TimeInterval(seconds)
        
        // Calculate miliseconds elapsed
        let miliseconds = UInt8(elapsedTime * 100)
        
        // Format output strings
        let minutesString = String(format: "%02d", minutes)
        let secondsString = String(format: "%02d", seconds)
        let milisecondsString = String(format: "%02d", miliseconds)
        
        // Update timer labels
        portraitTimer.text = "\(minutesString):\(secondsString):\(milisecondsString)"
        landscapeTimer.text = "\(minutesString):\(secondsString):\(milisecondsString)"
        
        // Update leaderboard timer
        completionTime = (Int(minutes), Int(seconds), Int(miliseconds))
    }
    
    // Custom function to resume the game after a short delay
    @objc func resumeGame() {
        
        // If the timer has reached zero, this will execute
        if seconds < 1 {
            // Stop the timer
            countDownTimer.invalidate()
            
            // If the tiles are a match, remove them from the board
            if selectedTiles[0].image == selectedTiles[1].image {
                for tile in selectedTiles {
                    removeTile(tile: tile)
                }
                
                // If all tiles have been removed, stop the play timer and trigger win indicators
                if tileCount == 0 {
                    playTimer.invalidate()
                    startTime = TimeInterval()
                    playTimer = Timer()
                    
                    // Set timer to one second and trigger a short delay before showing win indicators
                    seconds = 1
                    winDelay()
                }
                    
                    // Otherwise, enable interaction so user can continue playing
                else {
                    for tile in iPhoneTiles {
                        tile.isUserInteractionEnabled = true
                    }
                }
            }
                
                // If the tiles are not a match, this will fire
            else {
                // Flip selected tiles back over and remove from selectedTiles array
                for tile in selectedTiles {
                    faceDown(tile: tile)
                    
                    selectedTiles = []
                    
                    // Enable interaction for remaining tiles
                    for tile in iPhoneTiles {
                        tile.isUserInteractionEnabled = true
                    }
                }
            }
        }
            
            // If the timer has not yet reached zero, decrement by one second
        else {
            seconds -= 1
        }
    }
    
    // Custom function to indicate that the user has won after a short delay
    @objc func winGame() {
        
        // If the timer has reached zero, this will fire
        if seconds < 1 {
            // Stop the countdown
            countDownTimer.invalidate()
            
            // Show Play button
            playButton.image = #imageLiteral(resourceName: "Play")
            
            // Show win indicators
            youWinGraphic.isHidden = false
            finalTimeLabel.isHidden = false
            landscapeFinalTimeLabel.isHidden = false
            
            // Indicate that the game is not running
            isGameRunning = false
            
            // Call custom function to play "win" sound
            playSound(withIdentifier: "win")
            
            // Stop background music
            backgroundPlayer.stop()
            
            // Record win date
            dateStamp = Date()
            
            recordWin()
        }
            
            // Otherwise, decrement one second
        else {
            seconds -= 1
        }
    }
    
    // Custom function to start game after the countdown has ended
    @objc func beginGame() {
        // Begin playing audio countdown at three seconds
        if seconds == 3 {
            playSound(withIdentifier: "countdown")
        }
        
        // When timer reaches zero, this will fire
        if seconds < 1 {
            // Start background music
            playSound(withIdentifier: "background")
            
            // Show Stop button
            playButton.isHidden = false
            playButton.image = #imageLiteral(resourceName: "Stop")
            
            // Show game timers
            portraitTimer.isHidden = false
            landscapeTimer.isHidden = false
            
            // Indicate that the game is running
            isGameRunning = true
            
            // Stop the countdown
            countDownTimer.invalidate()
            
            // Flip all tiles over so they are "face down"
            for tile in iPhoneTiles {
                faceDown(tile: tile)
                tile.isUserInteractionEnabled = true
            }
            
            // Play "go" sound
            playSound(withIdentifier: "go")
            
            // Reset timer
            seconds = 5
            
            // Start game timer
            let aSelector: Selector = #selector(ViewController.gameTimer)
            playTimer = Timer.scheduledTimer(timeInterval: 0.01, target: self, selector: aSelector, userInfo: nil, repeats: true)
            startTime = Date.timeIntervalSinceReferenceDate
        }
            
            // If timer has not yet reached zero, decrement one second
        else {
            seconds -= 1
        }
    }
    
    // Custom function to play sound effects
    func playSound(withIdentifier: String) {
        
        // Play appropriate sound effect based on identifier passed into funciton
        switch withIdentifier {
            
        // Play background music
        case "background":
            // Reference file path
            let background = NSURL(fileURLWithPath: Bundle.main.path(forResource: "background", ofType: "aiff")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound based on file path
            do {
                try backgroundPlayer = AVAudioPlayer(contentsOf: background as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Set sound to loop until stopped
            backgroundPlayer.numberOfLoops = -1
            
            // Prepare and play sound
            backgroundPlayer.prepareToPlay()
            backgroundPlayer.play()
            
        // Play audio countdown
        case "countdown":
            // Reference file path
            let countdown = NSURL(fileURLWithPath: Bundle.main.path(forResource: "countdown", ofType: "wav")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound based on file path
            do {
                try countdownPlayer = AVAudioPlayer(contentsOf: countdown as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Prepare and play sound
            countdownPlayer.prepareToPlay()
            countdownPlayer.play()
            
        // Play "flip" sound
        case "flip":
            // Reference file path
            let flip = NSURL(fileURLWithPath: Bundle.main.path(forResource: "flip", ofType: "wav")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound from file path
            do {
                try flipPlayer = AVAudioPlayer(contentsOf: flip as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Prepare and play sound
            flipPlayer.prepareToPlay()
            flipPlayer.play()
            
        // Play "go" sound
        case "go":
            // Reference file path
            let go = NSURL(fileURLWithPath: Bundle.main.path(forResource: "go", ofType: "wav")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound from file path
            do {
                try goPlayer = AVAudioPlayer(contentsOf: go as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Prepare and play sound
            goPlayer.prepareToPlay()
            goPlayer.play()
            
        // Play "pop" sound
        case "pop":
            // Reference file path
            let pop = NSURL(fileURLWithPath: Bundle.main.path(forResource: "pop", ofType: "wav")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound from file path
            do {
                try popPlayer = AVAudioPlayer(contentsOf: pop as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Prepare and play sound
            popPlayer.prepareToPlay()
            popPlayer.play()
            
        // Play "win" sound
        case "win":
            // Reference file path
            let win = NSURL(fileURLWithPath: Bundle.main.path(forResource: "win", ofType: "wav")!)
            
            // Attempt to create audio session
            do {
                try AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Attempt to build sound from file path
            do {
                try winPlayer = AVAudioPlayer(contentsOf: win as URL)
            }
                
            catch {
                print(error.localizedDescription)
            }
            
            // Prepare and play sound
            winPlayer.prepareToPlay()
            winPlayer.play()
            
        // Indicate that an invalid sound was referenced
        default:
            print("Invalid Sound File")
        }
    }
    
    // Custom function to flip tiles so they are "face down"
    func faceDown(tile: UIImageView) {
        // Flip tile from left to right, change background color to orange, and reference Balloon image
        UIImageView.transition(with: tile, duration: 0.5, options: .transitionFlipFromLeft, animations: {tile.image = #imageLiteral(resourceName: "FaceUp")}, completion: nil)
        tile.layer.backgroundColor = defaultTileColor
        
        // Call custom function to play "flip" sound when tiles are flipped back over
        playSound(withIdentifier: "flip")
    }
    
    // Custom function to flip tiles so they are "face up"
    func faceUp(tile: UIImageView) {
        // Flip tile from right to left, change background color to white, and reference the appropriate image for the selected tile
        UIImageView.transition(with: tile, duration: 0.5, options: .transitionFlipFromRight, animations: {tile.image = self.iPhoneTileImages[tile.tag]}, completion: nil)
        tile.layer.backgroundColor = UIColor.white.cgColor
        
        // Call custom function to play "flip" sound when tiles are flipped back over
        playSound(withIdentifier: "flip")
    }
    
    // Custom function to remove tile from play area
    func removeTile(tile: UIImageView) {
        UIImageView.transition(with: tile, duration: 0.5, options: .transitionFlipFromTop, animations: {tile.isHidden = true}, completion: nil)
        //tile.layer.backgroundColor = defaultTileColor
        //tile.isHidden = true
        tile.isUserInteractionEnabled = false
        selectedTiles = []
        tileCount -= 1
        
        // This will play when tiles are removed from the board
        playSound(withIdentifier: "pop")
    }
    
    // Custom function to proceed after player enters their name
    func proceed() {
        // Reset move count
        totalMoves = 0
        
        // Call custom function to verify class size for current device
        phoneOrTablet()
        
        // Hide play button and win indicators
        playButton.isHidden = true
        youWinGraphic.isHidden = true
        finalTimeLabel.isHidden = true
        landscapeFinalTimeLabel.isHidden = true
        portraitTimer.isHidden = true
        landscapeTimer.isHidden = true
        
        // Tracks current index to populate play tiles
        var currentIndex = 0
        
        // Store shuffled tile images
        var iPhoneTileImagesShuffled: [UIImage] = []
        
        // Shuffle tile images and store in temporary array
        for _ in 0..<iPhoneTileImages.count {
            let randomIndex = Int(arc4random_uniform(UInt32(iPhoneTileImages.count)))
            
            iPhoneTileImagesShuffled.append(iPhoneTileImages[randomIndex])
            
            iPhoneTileImages.remove(at: randomIndex)
        }
        
        // Reference shuffled tile images
        iPhoneTileImages = iPhoneTileImagesShuffled
        
        // Show shuffled images in play tiles
        for tile in iPhoneTiles {
            tile.isHidden = false
            tile.layer.backgroundColor = UIColor.white.cgColor
            tile.image = iPhoneTileImages[currentIndex]
            tile.isUserInteractionEnabled = false
            
            currentIndex += 1
        }
        
        // Reset current index
        currentIndex = 0
        
        // Call custom function to start countdown to game start
        countDown()
    }
    
    // Custom function to update leaderboard
    func recordWin() {
        
        // Record current score information to be recorded
        let newEntry: (playerName: String, completionTime: (minutes: Int, seconds: Int, miliseconds: Int), totalMoves: Int, dateStamp: Date) = (playerName, completionTime, totalMoves, dateStamp)
        
        // Add the new score to the leaderboard
        leaderBoard.append(newEntry)
        
        // If there is more than one score on the leaderboard, sort them according to completion time
        if leaderBoard.count > 1 {
            
            // Track current index
            for _ in 0...leaderBoard.count - 1 {
                var currentIndex = 0
                
                // Loop through each score and sort
                for score in leaderBoard {
                    
                    // If the current score isn't the first on the list, compare to the one in front of it
                    if currentIndex > 0 {
                        
                        // If the current score has fewer minutes, move it up one space
                        if score.completionTime.minutes < leaderBoard[currentIndex - 1].completionTime.minutes {
                            leaderBoard.remove(at: currentIndex)
                            leaderBoard.insert(score, at: currentIndex - 1)
                        }
                            
                        // If the current score's minutes are equal to the one before it, compare seconds
                        else if score.completionTime.minutes == leaderBoard[currentIndex - 1].completionTime.minutes {
                            
                            // If the current score has fewer minutes, move it up one space
                            if score.completionTime.seconds < leaderBoard[currentIndex - 1].completionTime.seconds {
                                leaderBoard.remove(at: currentIndex)
                                leaderBoard.insert(score, at: currentIndex - 1)
                            }
                                
                            // If the current score's seconds are equal to the one before it, compare miliseconds
                            else if score.completionTime.seconds == leaderBoard[currentIndex - 1].completionTime.seconds {
                                
                                // If the current score has fewer miliseconds, move it up one space
                                if score.completionTime.miliseconds < leaderBoard[currentIndex - 1].completionTime.seconds {
                                    leaderBoard.remove(at: currentIndex)
                                    leaderBoard.insert(score, at: currentIndex - 1)
                                }
                            }
                        }
                    }
                    
                    // Increase index by one
                    currentIndex += 1
                }
            }
        }
        
        // If there are more than 5 scores on the leaderboard, remove the last one
        if leaderBoard.count > 5 {
            leaderBoard.removeLast()
        }
        
        // Create fetch request to clear leaderboard in core data
        let fetchRequest = NSFetchRequest<NSManagedObject>(entityName: "Leaderboard")
        
        do {
            let results: [NSManagedObject] = try managedContext.fetch(fetchRequest)
            
            // Loop through each score stored in core data and remove it
            for score in results {
                managedContext.delete(score)
            }
        }
        
        catch {
            print("Error clearing leaderboard")
        }
        
        // Loop through each score on the current leader board and save it to core data
        for score in leaderBoard {
            
            // Reference the correct entity description and context
            savedScore = NSManagedObject(entity: entityDescription, insertInto: managedContext)
            
            // Write player name, completion time, total moves, and date stamp to core data
            savedScore.setValue(score.playerName, forKey: "playerName")
            savedScore.setValue(score.completionTime.minutes, forKey: "playerMinutes")
            savedScore.setValue(score.completionTime.seconds, forKey: "playerSeconds")
            savedScore.setValue(score.completionTime.miliseconds, forKey: "playerMiliseconds")
            savedScore.setValue(score.totalMoves, forKey: "playerMoves")
            savedScore.setValue(score.dateStamp, forKey: "playerDate")
            
            // Save core data to memory
            (UIApplication.shared.delegate as! AppDelegate).saveContext()
        }
    }
}
