package org.example;
import java.util.stream.IntStream;

public class User {

    int [] ranks = {-8,-7,-6,-5,-4,-3,-2,-1,1,2,3,4,5,6,7,8};
    int rank = ranks[0];
    int progress = 0;

    //due to level differences method calculate progressPoints - actual value sum with new calculated value
    public void incProgress (int rank) {

        //mind stop score in ranking (-8, 8)
        if (this.rank == 8) {
            this.rank = 8;
            this.progress = 0;
            return;
        } else if (rank < ranks [0] || rank > ranks[ranks.length-1] || rank == 0) {
            throw new IllegalArgumentException("Number is not a valid value.");
        }

        //continue if actual rank is between -8 and 8

        /*
        check where on axis is numbers represents rank as parameter in constructor of method
        incProgress and rank as parameter of class (16 points length: index 0; index 15). Named
        "taskRankValue" and actual level "actualRankValue"
        */
        int tasksRankValue = IntStream.range(0, ranks.length)
                .filter(i -> ranks[i] == rank)
                .findFirst()
                .orElse(-1);

        int actualRankValue = IntStream.range(0, ranks.length)
                .filter(i -> ranks[i] == this.rank)
                .findFirst()
                .orElse(-1);

        int levelDifference = tasksRankValue - actualRankValue;

        //algorithm responsible for keeping or uplifting progress (+3;+1;0;x*d*d)
        if (levelDifference == 0) {
            progress += 3;
            return;
        } else if (levelDifference == -1) {
            progress += 1;
            return;
        } else if (levelDifference <= -2) {
            progress +=0;
            return;
        } else
            progress += 10*levelDifference*levelDifference;

        if (progress >= 100) {
            rankingUp();
        }

    }


    //other methods

    public void rankingUp () {
        //modify ranking. Started at -8 and update.
        int levelUp = (int) Math.floor(progress / 100);
        int actualRankValue = IntStream.range(0, ranks.length)
                .filter(i -> ranks[i] == rank)
                .findFirst()
                .orElse(-1);

        rank = ranks[actualRankValue + levelUp];

        //keeping maximal rank with 0 points in progress
        if (this.rank >= 8) {
            progress = 0; // Reset progress to 0 if rank is 8
        } else
            setProgressPoints();
    }

    /*
    supervise progressPoints - only xx size is available (over 100 points -> 0),
    remains will preserve
    */
    public void setProgressPoints () {
        int newProgressPoints = progress % 100;
        progress = 0;
        progress += newProgressPoints;
    }
}


/*
TASK

Write a class called User that is used to calculate the amount that a user will progress through a ranking system similar to the one Codewars uses.

Business Rules:
A user starts at rank -8 and can progress all the way to 8.
There is no 0 (zero) rank. The next rank after -1 is 1.
Users will complete activities. These activities also have ranks.
Each time the user completes a ranked activity the users rank progress is updated based off of the activity's rank
The progress earned from the completed activity is relative to what the user's current rank is compared to the rank of the activity
A user's rank progress starts off at zero, each time the progress reaches 100 the user's rank is upgraded to the next level
Any remaining progress earned while in the previous rank will be applied towards the next rank's progress (we don't throw any progress away). The exception is if there is no other rank left to progress towards (Once you reach rank 8 there is no more progression).
A user cannot progress beyond rank 8.
The only acceptable range of rank values is -8,-7,-6,-5,-4,-3,-2,-1,1,2,3,4,5,6,7,8. Any other value should raise an error.
The progress is scored like so:

Completing an activity that is ranked the same as that of the user's will be worth 3 points
Completing an activity that is ranked one ranking lower than the user's will be worth 1 point
Any activities completed that are ranking 2 levels or more lower than the user's ranking will be ignored
Completing an activity ranked higher than the current user's rank will accelerate the rank progression. The greater the difference between rankings the more the progression will be increased. The formula is 10 * d * d where d equals the difference in ranking between the activity and the user.
Logic Examples:
If a user ranked -8 completes an activity ranked -7 they will receive 10 progress
If a user ranked -8 completes an activity ranked -6 they will receive 40 progress
If a user ranked -8 completes an activity ranked -5 they will receive 90 progress
If a user ranked -8 completes an activity ranked -4 they will receive 160 progress, resulting in the user being upgraded to rank -7 and having earned 60 progress towards their next rank
If a user ranked -1 completes an activity ranked 1 they will receive 10 progress (remember, zero rank is ignored)
Code Usage Examples:
User user = new User();
user.rank; // => -8
user.progress; // => 0
user.incProgress(-7);
user.progress; // => 10
user.incProgress(-5); // will add 90 progress
user.progress; // => 0 // progress is now zero
user.rank; // => -7 // rank was upgraded to -7
Note: In Java some methods may throw an IllegalArgumentException.

Note: Codewars no longer uses this algorithm for its own ranking system. It uses a pure Math based solution that gives consistent results no matter what order a set of ranked activities are completed at.
 */