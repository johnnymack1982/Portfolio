//
//  Array_EXT.swift
//  Keep Calm and Calendar
//
//  Created by Johnny Mack on 10/7/18.
//  Copyright © 2018 John Mack. All rights reserved.
//

import Foundation

extension Array where Element: Hashable {
    func removingDuplicates() -> [Element] {
        var addedDict = [Element: Bool]()
        
        return filter {
            addedDict.updateValue(true, forKey: $0) == nil
        }
    }
    
    mutating func removeDuplicates() {
        self = self.removingDuplicates()
    }
}
