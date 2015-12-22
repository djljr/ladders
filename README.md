# The What

Create a general-purpose, beautiful, customizable leaderboard.


# Goals

- **Customizable**
	- **Ranking Algorithm.** It should be easy to change the algorithm applied to create the rankings and as a result you should be able to recreate the rankings based on that algorithm.
	- **Data Model.** The data model should be able to consider custom data that can be used by the ranking algorithm.
	- **Look and Feel.** It should be easy to customize the look and feel of the application.
- **Testing.** Have enough tests and in the right places that we feel confident when making changes do not introduce regressions. Expound on this!
- **Easy to deploy.** It should be a one step process to deploy to Amazon S3 as a Docker container.
- **Datomic.** We want to use Datomic.

# Goals Down The Road

- Multiple leagues to support the same user being in different leagues or tables, etc
- Keep track of game states within a game (for example, keep track of the scoring of your ping pong game)
