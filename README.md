# The What

Create a general-purpose, beautiful, customizable leaderboard.

# Development Environment

## REPL

You'll need 2 repls to do this properly. First start a figwheel repl using `lein figwheel`. You need to do this from the terminal for now since you don't get the `project.clj` profile merging if you start it from a repl using `figwheel-sidecar`. Still working on this one. It is highly recommended to use `rlwrap` for this repl to get normal readline functionality.

Second start up a clojure repl using `lein repl` from a terminal or `M-x cider-jack-in` from emacs. Then start the web server using `(start-server)` - this command lives in the `ladders.repl` package in `env/dev/ladders/clj/repl.clj`. You should now be able to see the running app on [localhost:3000].

## Dev Cards

From the figwheel repl, invoke `(switch-to-build devcards)` and the build will switch from the `app` to `devcards`. You can see the cards on [localhost:3000/cards]. You can go back by invoking `(switch-to-build app)`.

# Goals

- **Customizable**
	- **Ranking Algorithm.** It should be easy to change the algorithm applied to create the rankings and as a result you should be able to recreate the rankings based on that algorithm.
	- **Data Model.** The data model should be able to consider custom data that can be used by the ranking algorithm.
	- **Look and Feel.** It should be easy to customize the look and feel of the application.
- **Easy to deploy.** It should be a one step process to deploy to Amazon S3 as a Docker container.
- **Datomic.** We want to use Datomic.

# Goals Down The Road

- Multiple leagues to support the same user being in different leagues or tables, etc
- Keep track of game states within a game (for example, keep track of the scoring of your ping pong game)
