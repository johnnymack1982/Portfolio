//
//  File.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/3/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

public class Post: Codable {
    
    
    
    // Class properties
    var postMessage: String
    var timeStamp: Date
    var posterId: String
    var posterName: String
    var hasImage = false
    
    
    
    // Constructor
    init(PostMessage postMessage: String, TimeStamp timeStamp: Date, PosterId posterId: String, PosterName posterName: String, HasImage hasImage: Bool) {
        self.postMessage = postMessage
        self.timeStamp = timeStamp
        self.posterId = posterId
        self.posterName = posterName
        self.hasImage = hasImage
    }
    
    
    
    // Getters / Setters
    func getPostMessage() -> String {
        return postMessage
    }
    
    func setPostMessage(PostMessage postMessage: String) {
        self.postMessage = postMessage
    }
    
    func getTimeStamp() -> Date {
        return timeStamp
    }
    
    func setTimeStamp(TimeStamp timeStamp: Date) {
        self.timeStamp = timeStamp
    }
    
    func getPosterId() -> String {
        return posterId
    }
    
    func setPosterId(PosterId posterId: String) {
        self.posterId = posterId
    }
    
    func getPosterName() -> String {
        return posterName
    }
    
    func setPosterName(PosterName posterName: String) {
        self.posterName = posterName
    }
    
    func getHasImage() -> Bool {
        return hasImage
    }
    
    func setHasImage(HasImage hasImage: Bool) {
        self.hasImage = hasImage
    }
    
    
    
    // Custom methods
    func getPostId() -> String {
        return posterId + timeStamp.toMillis()!.description
    }
}



extension Date {
    
    func toMillis() -> Int64! {
        return Int64(self.timeIntervalSince1970 * 1000)
    }
    
    init(millis: Int64) {
        self = Date(timeIntervalSince1970: TimeInterval(millis / 1000))
        self.addTimeInterval(TimeInterval(Double(millis % 1000) / 1000 ))
    }
}
