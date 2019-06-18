//
//  Request.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/12/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation

public class Request: Codable {
    
    
    
    // Class properties
    var requesterName: String
    var requesterId: String
    var requestMessage: String
    var requestStatus: Int
    var timeStamp: Date
    var firstApprover: String?
    
    
    
    // Constructor
    init(RequesterName requesterName: String, RequesterId requesterId: String, RequestMessage requestMessage: String, TimeStamp timeStamp: Date) {
        self.requesterName = requesterName
        self.requesterId = requesterId
        self.requestMessage = requestMessage
        self.requestStatus = 0
        self.timeStamp = timeStamp
    }
    
    
    
    // Getters / Setters
    public func getRequesterName() -> String {
        return requesterName
    }
    
    public func setRequesterName(RequesterName requesterName: String) {
        self.requesterName = requesterName
    }
    
    public func getRequesterId() -> String {
        return requesterId
    }
    
    public func setRequesterId(RequesterId requesterId: String) {
        self.requesterId = requesterId
    }
    
    public func getRequestMessage() -> String {
        return requestMessage
    }
    
    public func setRequestMessage(RequestMessage requestMessage: String) {
        self.requestMessage = requestMessage
    }
    
    public func getRequestStatus() -> Int {
        return requestStatus
    }
    
    public func setRequestStatus(RequestStatus requestStatus: Int) {
        self.requestStatus = requestStatus
    }
    
    public func getTimeStamp() -> Date {
        return timeStamp
    }
    
    public func setTimeStamp(TimeStamp timeStamp: Date) {
        self.timeStamp = timeStamp
    }
    
    public func getFirstApprover() -> String? {
        return firstApprover
    }
    
    public func setFirstApprover(FirstApprover firstApprover: String?) {
        self.firstApprover = firstApprover
    }
    
    
    
    // Custom methods
    public func getRequestId() -> String {
        return requesterId + timeStamp.toMillis()!.description
    }
}
