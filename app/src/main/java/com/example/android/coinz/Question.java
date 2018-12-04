package com.example.android.coinz;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String title;
    private String correctAnswer;
    private List<String> answers;

    private Question(String title, String correctAnswer, List<String> answers) {
        this.title = title;
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    public String getTitle() {
        return title;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getAnswers() {
        return answers;
    }

    // Create questions
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Edinburgh Zoo is home to the only giant pandas in the UK. The female is called Tian Tian, but what is the name of the male?",
                "Yang Guang", new ArrayList<String>(){{ add("Yang Guang"); add("Bai Yun"); add("Su Lin"); }}));
        questions.add(new Question("Which Edinburgh area has the highest population density?",
                "Leith Walk", new ArrayList<String>(){{add("New Town"); add("Leith Walk"); add("Gorgie"); }}));
        questions.add(new Question("There are two statues of Queen Victoria in Edinburgh. One is at the Foot of Leith Walk. Where is the other?",
                "Atop the Royal Scottish Academy on Princes Street.", new ArrayList<String>(){{add("In the grounds of Holyrood Palace."); add("Atop the King's Theatre at Tollcross."); add("Atop the Royal Scottish Academy on Princes Street."); }}));
        questions.add(new Question("Where in Edinburgh can you find Dolly the Sheep?",
                "National Museum of Scotland", new ArrayList<String>(){{add("Holyrood Palace"); add("National Museum of Scotland"); add("Roslin Institute"); }}));
        questions.add(new Question("What is the tallest building in Edinburgh?",
                "St Mary's Episcopal Cathedral", new ArrayList<String>(){{add("St Mary's Episcopal Cathedral"); add("David Hume Tower"); add("The Scott Monument"); }}));
        questions.add(new Question("Why did people originally spit on the Heart of Midlothian?",
                "To show disgust", new ArrayList<String>(){{add("To gain wealth"); add("For good luck"); add("To show disgust"); }}));
        questions.add(new Question("What are the names of the Omni Centre giraffes?",
                "Martha & Gilbert", new ArrayList<String>(){{add("Abercrombie & Fitch"); add("Martha & Gilbert"); add("Bang & Olufsen"); }}));
        questions.add(new Question("Which figure is the Scott Monument named after?",
                "Sir Walter Scott", new ArrayList<String>(){{add("Sir Walter Scott"); add("Scott Hastings"); add("Scott of the Antarctic"); }}));
        questions.add(new Question("Who is the patron saint of Edinburgh?",
                "St Giles", new ArrayList<String>(){{add("St Cuthbert"); add("St Andrew"); add("St Giles"); }}));
        questions.add(new Question("What was the estimated population of the City of Edinburgh in 2016?",
                "507,170", new ArrayList<String>(){{add("638,547"); add("490,868"); add("507,170"); }}));
        questions.add(new Question("What time does the One o'clock Gun fire on a Sunday?",
                "It doesn't.", new ArrayList<String>(){{add("12:59pm"); add("At 1pm."); add("It doesn't."); }}));
        questions.add(new Question("Edinburgh has two pubs named the Black Bull. One is in the Grassmarket. Where is the other?",
                "Leith Street", new ArrayList<String>(){{add("Leith Street"); add("Elm Row"); add("Bruntsfield Place"); }}));
        questions.add(new Question("Which of these is the oldest building in Edinburgh?",
                "St Margaret's Chapel", new ArrayList<String>(){{add("John Knox House"); add("Holyrood Palace"); add("St Margaret's Chapel"); }}));
        questions.add(new Question("Which European city is Edinburgh most often compared to?",
                "Athens", new ArrayList<String>(){{add("Stockholm"); add("Athens"); add("Bucharest"); }}));
        return questions;
    }
}
