class Twitter {

    int timestamp;
    int feedSize;

    Map<Integer, Set<Integer>> userFollowsMap;
    Map<Integer, List<Tweet>> userTweetsMap;

    /** Initialize your data structure here. */
    public Twitter() {
        // init
        timestamp = 0;
        feedSize = 10;

        userFollowsMap = new HashMap<>();
        userTweetsMap = new HashMap<>();
    }

    /** Compose a new tweet. */
    public void postTweet(int userId, int tweetId) {
        // check
        isFirstTime(userId);
        // post it
        List<Tweet> tweetList = userTweetsMap.get(userId);
        tweetList.add(new Tweet(tweetId, ++timestamp));
    }

    /** Retrieve the 10 most recent tweet ids in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user herself. Tweets must be ordered from most recent to least recent. */
    public List<Integer> getNewsFeed(int userId) {
        // check
        isFirstTime(userId);
        // custom comparator
        Comparator<Tweet> comparator = new Comparator<>() { // minHeap
            public int compare(Tweet a, Tweet b) {
                return a.createdAt - b.createdAt;
            }
        };

        PriorityQueue<Tweet> pq = new PriorityQueue<>(comparator);
        Set<Integer> followees = userFollowsMap.get(userId);
        for(int followee: followees) {
            List<Tweet> followeeTweet = userTweetsMap.get(followee);

            for(Tweet tweet: followeeTweet) {
                pq.add(tweet);
                if(pq.size() > feedSize) {
                    pq.poll();
                }
            }
        }

        LinkedList<Integer> recentFeed = new LinkedList<>();
        int count = feedSize;

        while(count > 0) {
            Tweet tweet = pq.poll();
            if(tweet == null) return recentFeed;
            recentFeed.addFirst(tweet.tweetId);
            count--;
        }
    }

    /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
    public void follow(int followerId, int followeeId) {
        // check
        isFirstTime(followerId);
        isFirstTime(followeeId);

        // add followee
        Set<Integer> followees = userFollowsMap.get(followerId);
        followees.add(followeeId);
    }

    /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
    public void unfollow(int followerId, int followeeId) {
        // check
        isFirstTime(followerId);
        isFirstTime(followeeId);

        // remove followee
        Set<Integer> followees = userFollowsMap.get(followerId);
        if(followeeId != followerId) {
            followees.remove(followeeId);
        }
    }


    /**

     userFollowsMap {
     u1: {u2},
     u2: {u1, u3},
     u3: {}
     }

     userTweetsMap {
     u1 : [t1, t2, t3],
     u2 : [t4, t5, t6]
     u3 : [t7]
     }

     **/
    private void isFirstTime(int userId) {
        Set<Integer> followees = userFollowsMap.get(userId);
        if(followees == null) {
            followees = new HashSet<>();
            followees.add(userId);
            userFollowsMap.put(userId, followees);
        }

        List<Tweet> tweets = userTweetsMap.get(userId);
        if(tweets == null) {
            tweets = new LinkedList<>();
            userTweetsMap.put(userId, tweets);
        }
    }
}

class Tweet {
    int tweetId;
    int createdAt;

    Tweet(int tweetId, int createdAt) {
        this.tweetId = tweetId;
        this.createdAt = createdAt;
    }
}

/**
 * Your Twitter object will be instantiated and called as such:
 * Twitter obj = new Twitter();
 * obj.postTweet(userId,tweetId);
 * List<Integer> param_2 = obj.getNewsFeed(userId);
 * obj.follow(followerId,followeeId);
 * obj.unfollow(followerId,followeeId);
 */