//
//  File.swift
//  Fam.ly - iOS Edition
//
//  Created by Johnny Mack on 6/3/19.
//  Copyright © 2019 John Mack. All rights reserved.
//

import Foundation
import FirebaseAuth
import FirebaseFirestore
import FirebaseStorage
import UIKit

public class PostUtils {
    
    
    
    // Custom methods
    // Custom method to create post
    public static func createPost(Post post: Post, Controller controller: UIViewController, Photo photo: UIImage?) {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Map post for upload
        let postData: [String: Any] = [
            "postMessage": post.getPostMessage(),
            "timeStamp": post.getTimeStamp(),
            "posterId": post.getPosterId(),
            "posterName": post.getPosterName(),
            "hasImage": post.getHasImage()
        ]
        
        // Reference database location and attempt to upload to newsfeed
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(post.getPostId()).setData(postData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
                // If successful, reference database location for timeline and attempt to upload
            else {
                database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + post.getPosterId()).collection("posts").document(post.getPostId()).setData(postData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                    
                    // If successful, upload post photo
                    else {
                        if photo != nil {
                            let storage = Storage.storage()
                            let storageRef = storage.reference()
                            let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + post.getPostId() + ".jpg")
                            let imageData = photo!.jpegData(compressionQuality: 0.5)
                            photoRef.putData(imageData!)
                        }
                        
                        // Let the user know the post was successfully created
                        let alert = UIAlertController(title: "Success", message: "Post created", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
    
    // Custom method to listen for newsfeed updates
    public static func listenForNews() {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Reference date seven days in the past
        let sevenDaysAgo = Calendar.current.date(byAdding: .day, value: -7, to: Date())
        
        // Reference database location and listen for updates
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").whereField("timeStamp", isGreaterThanOrEqualTo: sevenDaysAgo!).addSnapshotListener { querySnapshot, error in
            guard let documents = querySnapshot?.documents else {
                print("Error fetching documents: \(error!)")
                return
            }
            
            var posts = [Post]()
            
            // Loop through posts and add to post list
            for document in documents {
                let postMessage = document.get("postMessage") as? String
                let timeStamp = document.get("timeStamp") as? Date
                let posterId = document.get("posterId") as? String
                let posterName = document.get("posterName") as? String
                let hasImage = document.get("hasImage") as? Bool
                
                let post = Post(PostMessage: postMessage!, TimeStamp: timeStamp!, PosterId: posterId!, PosterName: posterName!, HasImage: hasImage!)
                
                posts.append(post)
            }
            
            // Sort newsfeed
            posts = posts.sorted(by: {$0.getTimeStamp().compare($1.getTimeStamp()) == .orderedDescending})
            
            // Save newsfeed to file
            let encodedObject = try? JSONEncoder().encode(posts)
            if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
                let fileName = getDocumentsDirectory().appendingPathComponent("newsfeed.fam")
                
                do {
                    // Attempt to save JSON string to file
                    try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
                }
                    
                catch {
                    print("Failed to write data to file")
                }
            }
        }
    }
    
    // Custom method to get documents directory
    public static func getDocumentsDirectory() -> URL {
        let paths = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)
        return paths[0]
    }
    
    // Custom method to load newsfeed from file
    public static func loadNewsfeed() -> [Post] {
        let fileName = getDocumentsDirectory().appendingPathComponent("newsfeed.fam")
        var posts = [Post]()
        
        do {
            let jsonString = try String(contentsOf: fileName)
            if let jsonData = jsonString.data(using: .utf8) {
                
                do {
                    try posts = JSONDecoder().decode([Post].self, from: jsonData)
                }
                
                catch {
                    print("Error loading newsfeed")
                }
            }
        }
            
        catch {
            print("Error loading account from file")
        }
        
        return posts
    }
    
    // Custom method to load post image from remote storage
    public static func loadPostImage(View postImage: UIImageView, PostId postId: String, ImageHeight imageHeight: NSLayoutConstraint) {
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + postId + ".jpg")
        
        photoRef.getData(maxSize: 1 * 1024 * 1024) { data, error in
            if error != nil {
                postImage.image = nil
            }
            
            else {
                let image = UIImage(data: data!)
                postImage.image = image
            }
        }
    }
    
    // Custom method to delete selected post
    public static func deletePost(PostId postId: String, PosterId posterId: String, Controller controller: UIViewController) {
        
        // Reference database
        let database = Firestore.firestore()
        
        // Reference remote storage location for post photo
        let storage = Storage.storage()
        let storageRef = storage.reference()
        let photoRef = storageRef.child("photos/" + Auth.auth().currentUser!.uid + postId + ".jpg")
        
        // Reference database location for newsfeed and attempt to delete post
        database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(postId).delete() { err in
            if let err = err {
                print("Error removing document: \(err)")
            }
            
            // If successful...
            else {
                
                // Referece database location for timeline and attempt to delete post
                database.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + posterId).collection("posts").document(postId).delete() { err in
                    if let err = err {
                        print("Error removing document: \(err)")
                    }
                    
                    // If successful...
                    else {
                        
                        // Delete photo from remote storage
                        photoRef.delete(completion: nil)
                        
                        // Let the user know the post has been successfully deleted
                        let alert = UIAlertController(title: "Post Deleted", message: "This post has been successfully removed", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
    
    // Custom method to update selected post
    public static func updatePost(Post post: Post, Posts posts: [Post], Controller controller: UIViewController) {
        
        // Save post to file
        let encodedObject = try? JSONEncoder().encode(posts)
        if let encodedObjectJsonString = String(data: encodedObject!, encoding: .utf8) {
            let fileName = getDocumentsDirectory().appendingPathComponent("posts.fam")
            
            do {
                // Attempt to save JSON string to file
                try encodedObjectJsonString.write(to: fileName, atomically: true, encoding: String.Encoding.utf8)
            }
                
            catch {
                print("Failed to write data to file")
            }
        }
        
        // Map post for upload
        let postData: [String: Any] = [
            "postMessage": post.getPostMessage(),
            "timeStamp": post.getTimeStamp(),
            "posterId": post.getPosterId(),
            "posterName": post.getPosterName(),
            "hasImage": post.getHasImage()
        ]
        
        // Reference database location and attempt to upload to newsfeed
        let databse = Firestore.firestore()
        databse.collection("accounts").document(Auth.auth().currentUser!.uid).collection("newsfeed").document(post.getPostId()).setData(postData) { err in
            if let err = err {
                print("Error writing document: \(err)")
            }
            
            // If successful...
            else {
                
                // Reference database location and upload to timeline
                databse.collection("accounts").document(Auth.auth().currentUser!.uid).collection("profiles").document(Auth.auth().currentUser!.uid + post.getPosterId()).collection("posts").document(post.getPostId()).setData(postData) { err in
                    if let err = err {
                        print("Error writing document: \(err)")
                    }
                    
                    // If successful, let the user know
                    else {
                        let alert = UIAlertController(title: "Post Updated", message: "Your post has been successfully updated", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { (UIAlertAction) in
                            controller.performSegue(withIdentifier: "unwindToNewsfeed", sender: controller)
                        }))
                        controller.present(alert, animated: true)
                    }
                }
            }
        }
    }
}
