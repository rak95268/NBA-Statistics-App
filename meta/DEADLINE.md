# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

    This app allows a user to input a value between 1-30, one ID for each NBA team, to receive data
    about any team including its full name, conference, division, and abbreviation. It then loads
    the first image result that appears when the team name is searched on Google. I used the BallDontLie
    API to retrieve the data. Then I used that data, specifically the team name, as a query for the SerpStack
    API to retrieve the first image search.

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

    Something new that I learned from working on this project is how important Type objects are. I had a lot of
    difficulty in parsing through the SerpStack JSON because it kept returning the image url values as null, but
    using Type objects helped me work through this issue.


## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

    If I could start the project over from scratch, I'd come up with a much simpler idea. This project took
    genuinely forever, and unfortunately during my working progress I was dealing with loss in my family which
    made working on it that much more challenging.
