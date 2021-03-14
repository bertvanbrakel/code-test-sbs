# Question 2.1 - git hosting

Steps I'd take to improve security

  - Require ssh cert logins only and require certs to be password protected
  - move the access details out of the code repos and either (or a combo):
     - into a separate git repo with only credential details
         - possibly further breakup the credential repos into a separate repo per environment (local, dev, qa, staging, prod...)
         - only authorised staff have access to the credential repos and the deployment server
         - encrypt the credentials with only authorised staff and the deploy server having the private key (again different per env)
     - move credentials into an enterprise credential store / management solution. This is then applied at deploy time
     - move credentials behind a credentials api where services call it on starup (restricted by IP/certificates)
     - move the credentials onto an internal git repo and apply all the same security restrictions as mentioned above.
  - no application should hardcode any config details (except maybe defaults), especially no credential based config. This should all be
    injected in at runtime or deploy time 
  - regularly rotate keys

# Question 2.2 - Workflow

Things which need to be taken into consideration

- the workflow chosen would somewhat depend on the team and their appetite for git merging/rebase, and their discipline. A
more disciplined approach might involve more git branches and merges but keeps things more strictly correct. A less 
disciplined and experienced team might require a simpler flow with fewer branches and merges as the confusion cost
might be higher than the benefit of a more strict 'true' process.
- the length of time a release candidate stays in the QA/staging environments. If testing takes a long time
it may result in the next release backing up with a whole heap of features.
- the business's appetite for continuous delivery vs more managed release 'trains'
- whether deployments are based on git, or versioned binary artifacts (e.g. maven artifacts). In which case a bunch of 
  git branches can be ditched
  
## Assumptions

 - releases are not managed via sprints, but a continuous delivery model (e.g. kanban), and that when a
feature is ready to ship it is merged, tagged, and pushed via various environments out to production.
 - no need for multiple versions of a product to be maintained (aka there is only the 
current 'live' version). So basically a website

## Approach

Use signed commits instead of branches. One can always branch from a single commit

Reduce the use of long lived branches. They tend to get out of sync and confuse people

I would encourage the model of being able to get things to production quickly vs relying too heavily on overly 
bureaucratic stage gates to manage quality. The latter would lead to having fixes for known production bugs ready but
not being able to fast track it out to production. This however does require a good suite of automated tests, and the 
discipline to improve the tests as soon as a bug makes it to production. Initially it might mean more production bugs,
but this should very quickly diminish as more tests are added and lead to faster features delivery.

Keep in mind my suggestion below is subject to change as more of the team/business context becomes known.

## Model

I would initially suggest a model loosely based of the git-flow feature model. 

 - master branch. This is the production branch (it may be redundant but many people like it)
 - develop branch as the current unstable from which all normal release candidates ('rc') are created from
   - permanent branch
 - feature branches where all feature work is done on. This is created from the latest develop branch
   - these would only survive for the length of the feature development, After merge they're deleted
 - hotfix branches for any hotfix work. Branched from master
  - these would only exist for the length of the hotfix
-  pull request ('pr') branches. This would be managed automatically via a code review tool (e.g. Crucible, Gerrit, Github...)
  - these would exist only for the length of a PR
  
## Normal Process

- A feature branch is created from develop.
   - Work is done on the feature branch
   - Developers run it locally (and/or central sandboxes)
- A pull request to develop is raised (code review tool creates a new branch in 'pr')
- A successful pull request is merged back into develop (and the pr branch removed)
- Someone or an automated process kicks off a release to the dev environment from develop
    - this tags the commit with something like 'rc-123' (and signed)
- When the tests pass on dev environment, a new tag is created and signed to mark the given commit as passed 
    (e.g. 'rc-123-develop-passed')
- Someone or an automated process kicks off a release to the QA environment from the passed develop commit 
  ('rc-123-develop-passed'). 
- When the tests pass on QA environment (and maybe from QA sign off), a new tag is created and signed to mark the given 
  candidate as passed (e.g. 'rc-123-qa-passed')
- Same process for the staging environment as for QA, but triggered from the 'rc-123-qa-passed'
- Same process for the production environment.
  - In addition the production deployment may also set the tag 'rc-123-YYYY.MM.DD' to mark this release being the current 
    live (making it easier for devs/tools to know/run the production version locally without having to interrogate an api)
- possibly add another tag named 'prod-live', however this might cause git sync issues (warnings) when deleting/setting this tag
- the release is merged to master

_NOTE:_ maybe the QA/Staging deployments could also create tags like 'rc-123-qa-YYY.MM.DD' to know what was released when

_NOTE:_ the format 'YYY.MM.DD-rc-123-qa' might make it easier to sort release tags and find the last one

## Hotfix process

- A new hotfix branch is created from the tagged current production version ('rc-123' vs 'prod-live' if prod has been rolled back)
- Work is done on the hotfix branch
- A PR is raised to merge to develop and kick off the QA/staging process
- Possibly skip the dev environment to not interfere with existing development
- QA/Staging take their deployments from the hotfix branch (along with the same tags as before)
   - this prevents taking any existing in progress development work
- The hotfix branch is merged to master

_NOTE:_ The master branch is pretty redundant as all releases are via tags. It just makes some people happier to have one

# Question 2.3 - Code Quality

- Most importantly, encourage a good team culture which values excellence and practicing the craft. The team will come
up with solutions if given the opportunity and encouragement.
  - code reviews and pull requests. This is a two way learning exercise and a chance for team member to collaborate
  - regularly review existing code to see if it follows the current best practices. If not, update ruthlessly
    - note: this may break things more initially, but pain points will be removed quickly and code quality shoots up 
        and quality output increases. This also encourages the Single Responsiblity Principle
  - review code as a team. The good, the bad, and the ugly (in a non judgemental way)
    - this might involve the team picking these during the week and we have a friday afternoon learning session
    - has to be run carefully as people can get upset. But the idea is over time we trust each other and it's all about 
      the code, not about the person
  - encourage 'brown-bag' sessions where people can present a useful tool or technique to the team. This might lead to better
    designs, whole swaths of code being deleted and a more general interest in
  - encourage SOLID
  - maintain a recommended books list/library. Like Uncle Bob's 'Clean Code'
- I'm a fan of as much automation as reasonably possible as there is less chance for human error and the need to constantly
remind team members.
  - decide on consistent code formatting (I like the 'prettier' plugin as this decides much for us, avoiding conflicts) 
  - code formatter and linters applied to projects. Make it part of the git repo
    - git hooks on commit runs the linter and aborts the commit on failure  
  - automated testing
      - make sure the test tools are easy to use and it's easier and faster to create a test than do it manually
      - apply the same standards to test tools as to the production code
  - apply various plugins (in the case of maven/npm/pip/conda) to provide code quality reports (and act upon them)
  - run code coverage tools
  - if possible run 'fuzzers' on the code (so random code changes and see if tests pick up failures)
  - run third party audits to flag anything we missed  
- remove code no longer needed
- make dev environments consistent so more time can be spent on actually improving the product/code vs fighting setup 
  and configuration
  - e.g. use conda/docker/docker-compose. In the case of vscode use the 'devcontainer' feature. Or Ansible if using VM's
  - allow devs to run everything easily on their local machine  
- simplify designs
- keep a nice separation of concerns (don't mix update/read code)

# Question 3.1 - Website url

There is an attempt to access the passwd file. The attacker is hoping the code does something like

```
var filePath = DATA_DIR + '/' + idFromUrlParam
return readFile(filePath) <-- returns the contents of the /etc/passwd file
```

The corrective measures are:
- sanitise all input before using. Never trust user input
- add additional checks in when accessing files to make sure they are within a certain directory (so make absolute, 
  then check they start with allowed directories only). In case unsanitised values do get through 
- don't run a process with elevated (aka 'root') access. Though the passwd file is needing to be read by the apache process

In the case mentioned, only 'A-Za-z0-9-' should be allowed in the id (either strip illegal chars or fail the request). For 
a more relaxed approach all '.' and '/' can be removed before using the id (though then there are still unicode edge cases 
and concerns)

# Question 3.2 - link

This is a XSS vulnerability

What has occured: the url param 'search' is being used to insert a '<script>' tag into the page which then runs a 
javascript 'eval' statement with an alert. The search param is probably being used to generate html via string append

e.g.
'''
<div>Your search term was ${search}</div>
'''

The danger being this would allow an attacker to send someone a malicious url 
link with a payload to perform an action on the visited page (in this case 'sitea.com). This would allow the attacker to 
lift private information from the page (e.g. login credentials, cookies, bank details) and/or perform actions as the user
(e.g. invoke api's, make forum posts, send emails, spread more malicious links)

The prevention is to sanitise all input, and never string append to create html. The best approach would be to use an
allow list of chars the search term could use ('A-Za-z0-9_- '), or remove all the illegal chars (using a thridparty 
trusted and tested libray would be best). So remove anything like '/', '%' etc. Though there are also unicode chars that 
can be used to breakout so we need to be exhaustive. 

If using a client side library like Vue/React use this libraries dom appending code as they sanitise input 

Also use POST instead of GET and only use posted data (and not the url params). However this would reduce caching abilities
(if not customised per user)


# Question 3.3 - SQL injection

This has a SQl injection attack vulnerability. This uses string concatenation to create a sql query without escaping user input

The fix is to use SQL parameters.
```
$stmt = $conn->prepare('
    SELECT t1.id, t1.name, t2.category
    FROM table_1 t1
        JOIN table_2 t2 ON t1.id = t2.foreign_key
    WHERE 
        t1.date > DATE_SUB(NOW(), INTERVAL 1 MONTH) AND
        t2.category = ?
    ORDER BY t.date DESC
');

$category = $_GET['parameter'] 
$stmt->bind_param("s", $category);
$query = $stmt->execute();
$stmt->close();
```

_NOTE:_ I would prefer _named_ params rather than positional ones as it makes it easier to understand. I would assume
the mysql library would have one

# Question 3.4 - XSS scripting attack

This is a XSS attack. User Y has used holes in two sites to succeed with this attack. It is both sites A and B's 
responsibility to reduce this type of attack, but ultimately it is Site A's failure.

1. Site B has not validated user input by allowing user Y to post the malicious html. 
   - This is now in the Site-B's database and will be trusted by User-X's browser
   - the image is hidden, so it's not visible when someone is visiting the site, so it's hard to spot
2. Site A has allowed an important operation to be performed using a GET and not a POST. By loading the malicious html 
   (on Site B) the browser has performed a GET on 'http://www.sitea.com/?action=transfer-funds&amount=1000', and the page at that url 
   has used the 'action' param to invoke the transfer action. 
   - Site A should require a POST.
   - Site A should check the refer's url and only allow those from Site A (same origin policy)
   - Site A should require the user confirm an important action such as a funds transfer
   - Site A should use a transaction id in any funds transfer. 
      - This reduces potential for replay attacks as an action can only be performed once
      - If a user resubmits not two or more transfers occur
    
# Question 4.1 - Slow app

Most important is to identify and measure the slowness vs making random changes and hoping that would fix it.

1. Run the application myself first
2. I would first ask if anyone knows where the slow bits are. If it's known certain things are slow that would help me
focus on those bits first (so it depends if certain parts are slow or just 'general' slowness)
3. I would check the logs and see if anything useful is in there
4. I would increase the log level if nothing obvious comes up in the logs first time round
5. I would look for any performance test code and if found see what that comes up with
6. I would run the application locally and see what a profiler tells me about speed (in the java space something like retrace)
7. If nothing is obvious then I'd add timing annotations or similar into the code and log this (managed via config flags). It
   may be that database lock contention is the issue and only visible under load, so only get to see it in production. Or
   some routine is slow, or some api call
   
Based on the above I would formulate a response. If there is db lock contention, I would see what the cause is (maybe 
lock on read), or there are single points of contention which may be fixed with a different approach (reduce global locks). 
Also check the database lock policies (in the case of MS SQL it often locks tables/rows on read)

If the database is an issue I would instrument the database connection. I would also look at the SQL query plan.

Maybe a better caching strategy is useful

Also I would look at the data flow and see if anything can be improved here. Maybe something is hammering the api/database 
in a tight loop instead of a single call or caching in a local variable.

End of the day get some actual data and proceed from their

# Question 4.2 - Caching

The caching strategy would very much depend on the workflows, the content to be cached, existing technologies used, the
traffic load, how much the content is customised, how the site is accessed. 

Assuming it's something like a news website (like SBS), then I would split assets into categories

- the (versioned) static code (like javascript/css/static-images) can be served via a cdn (cloudfare/fastly)
- content binary assets can be served via a cdn if it's publicly available
    - the fastly cdn can perform request collapsing (so multiple cache hots for the same content) to reduce load on the 
    source of truth
- content binary assets behind a paywall/login could also be served over something like fastly but using their Varnish 
  language (VCL) or their new WASM on lucet to manage access to the content (possibly using JWT to determine access rights)
   - alternatively an in-house cdn can be used which would perform access checks 
- content names would be hash based. This makes caching easier as doesn't require the need to invalidate caches, just an 
  update in the link to it (so an update to image1.jpeg wouldn't require a cache invalidation. It would just go from 
  image1-GHWEGHWGEHW.jpg to image1-HSDJKLHGASKJLDH.jpg)     
- dynamic per user content would have to be generated and have the cache header set to the appropriate length of time
   - this could also be cached on an internal cdn with access control code (and a TTL)
   - the backing data or raw output can also be cached in memory using something like a redis or memcache. Even arangodb 
    using a key-value collection might be good enough
- use memcahe/redis/other for the case of the social media share count (backed by the database)
   - updates to the db result in invalidation/update of the memcahe/redis value
   - content reads for this value would come from memcache/redis or loaded into it (from db or api) if not currently cached
- depending on frontend tech used things like webpack can be used to create javascript and image bundles for static assets

On the application design side of things, a mostly CRQS approach can be used (minus the dynamic bits). Reads come from 
CDN's, internal cached disks, redis/memcache. Updates go to the CMS/RDBMS, and reads come  from CDN's. 

The challenges to consider are cache invalidation (when should content be removed), access control to content, types of 
loads, determining when a particular asset (aka an image) is no longer referenced and can be permanently removed. 

Another challenge would be setting up suitable local/dev/qa/staging environments to replicate production as closely as 
possible to ensure when content gets to production it actually works (i.e. no dangling links)

# Question 4.3 - site with articles

- First issue is that as articles are added then all the articles files will have to be updated as files are bumped from
articles_1 to articles_2, and these are bumped to articles_3 etc. So a lot of files are changed. 
- This also makes caching harder
- On update of an article, it has to be found in the appropriate articles file

Assuming articles are published once a day, then my initial stab at it would be:

- articles_YYYYMMDD.xml contains the articles published on this particular day (if more frequently then a timestamp 
  could be added)
    - would contain about 300 links
- the sitemap would link to the files above in chronological order
    - articles_20200314.xml
    - articles_20200315.xml
- on republish/update of an article, the original publish timestamp would be used to find the appropriate article file
 and have this updated.
- note we can't just list every single article directly in the sitemap as the current limit is 50k links per sitemap

# Question 4.4 - CMS class

1. Create a subclass of the Article class for each third party platform (so WebSiteArticle, SyndicationArticle, AppleNewsArticle..)
2. Register a factory function for each article type in a dictionary (taking in the article content).
3. Get the factory function by article content type (so don't repeat the code)
4. Invoke the function and return the article

The Base article class would extract the title/description/body from the content and be overridden in any particular subclasses 

- This would allow for easier testing of each article type.
- This would allow for more customisation of articles per type
- This would reduce the size of the Article class (and not put all customisations in one single class)

# Question 4.5

## System One - Ferrari F1 Run Data Api

### Purpose

The purpose was to replace the existing desktop based COM application which served data to many applications with an API. After
trying to port the existing code it was decided to start over (after performance tests showed too many thread locking issues)

The data surfaced was all the data associated with a lap/car/race from both onboard realtime sensors, simulations, and data 
uploaded during pitstops via fibreoptic cable attached to the car

The api was to be flexible so that many different apps could make use of this without requiring customisation per app. At 
the same time we were rewriting the main race application used by the team during a race.

I wrote most of this on my own as we were a small team and others were busy rewriting other software (our entire team ws bought
in to replace the previous one)

### Technologies

API built using C# and MS Web Api, ServiceStack and MS SQL.

ServiceStack was used as it's approach was much better than the MS provided web stacks and it served us well. 
 - There were also ServiceStack clients we could use from C# to make things easier). 
 - ServieStack also allowed us to write handlers using just pure DTO's instead of having to pull them from the request 
object (so a single place handled mapping, validation, output transformation). Each handler mapped to an action url.
 - The handler approach also made it easier to unit test

Database access was via a single file based micro ORM (like dapper, but the name currently escapes me). This prevented
any SQl injection attacks and made mapping rows to DTO's easy. I had to expand this ORM for our usecase

I also used T4 templates (calling custom C# code) to generate database access code for both production access and test 
code access. The latter was interesting as readers, writers, verifiers were all code generated for the test (using partial 
classes). This made it very easy to setup test data in the tests. Test data was produced in the tests (via builders) and run 
against a local embedded database for local dev, and on a proper MS SQL in dev/qa/staging.

### How it worked

The api used a REST based approach with the ability to selectively include/exclude attributes/child-objects. These days 
it would be called a graphql like API.

e.g.

```
/api/race/1234?fields=laps,cars
/api/race/1234/laps?fields=start,end,time,car
```

Behind the scenes the SQL query would be built based on the request data (so whole joins could be avoided if certain data 
wasn't required). This gave applications the flexibility to decide on the data required.

The output could be xml or json.

Use was also made of linking in the returned data to improve cacheability. For example if the lap data contained car details
this could be returned inline as well as contain a link to the source 

```
/api/race/1234/cars/abc
```

As in

```
data:{
 laps:{
   "1":{
      start:"..",
      end:"..",
      number:"1",
      car:{
        link:"/api/race/1234/cars/abc",
        cache:{ ttl:".." },
        data:{
            id:"abc",
            car details....
        }
      }
   },....

 }
}
```

This allowed for single queries for all the data, as well as allow for automatic updates by simply referring to the link
to update the cache (it was destructured and cached on the client in a generic way)

### Problems solved 

1. We could surface a data model independent of the database
2. We could start migrating applications away from direct database access
3. It was easy for applications to get the data they needed with simple predictable urls
4. It was easy to refresh information
5. We could write proper tests
   - existing code had low test coverage
   - existing tests relied on static error prone db setup. So no one wanted to touch anything in case it broke things
   - it was faster to write a test than to test it manually
6. It was easier to write clients as data could be mocked, or cached for replay testing.

### Issues encountered

1. MS SQL appeared to lock tables on read. Took a while to find the cause in production
as it would only happen under certain circumstances. Better dumps on this error finally showed the cause.
- the fix was to change the locking policies at the server level. This tooka  while as the DB had to be taken 
  offline for a while and we had to make sure no data corruption after the change.
- this wouldn't have happened in an Oracle DB as it does versioned reads

2. The Sql generation was a bit hairy until a nice builder pattern approach was created. Making sure the query performed
well also took some time (there were lots of joins, and new views used). In the end it was very fast.
   
3. Lap data from multiple sources (official track data feed (poor), realtime data from our car via radio, fibre link 
   upload, and simulations)
   - the lap numbers didn't always match up or were missing and lap times didn't match exactly
   - I had to come up with some algorithm to line laps up (best fit) based on start times, times and order. That was a 
     fun challenge

_NOTE:_ We couldn't really cache results as this data was updated in realtime and sometime back filled (lap data from 
multiple locations)
  
### What I would do different

Given the constraints at the time, not much. 
- Probably not bother with the port in the first place, that wasted a bunch of time
- After getting feedback years after I'd left they weren't using the embedded link data much anymore. Seems no one
really uses the symantic web

Apart from that it's apparently still running with very few changes needed over the years as it was pretty flexible
as it was.


## System Two - Currently still unreleased it seems

A currently unreleased pysical exercise video site. I was bought in to help right a floundering project. 

### Purpose 

Provide physical workout videos people could subscribe to. Content creators would be paid based on the number of
people who followed them

I was responsible to firefight issues and also build the member, trainer and admin dashboards along with any required apis. 

### Technologies

Many of the decisions were already made when I joined. The main website used jquery and hugo (static site generator) to 
pull content from a CMS (forrestry.io) and deploy.

Netlify was already being used to build the site

The backend was google cloud firebase. Using JWT

For the dashboards I used VueJs, Vuex, Typescript. I used npm as the build tool. 

I introduced google cloud firebase functions to build out an api (using express/node). All written in typescript.

Initially the site used vimio for video delivery. It was requested we use alternatives due to cost and upload timeout 
issues. I ended up using google cloud storage as this integrated well with the api.

### How the technologies were used

The CMS branched the repo and pushed new content (as markdown files) into given folders. Then hugo was invoked to generate
the main site. The member/trainer/admin dashboards were built using npm and copied to a static output folder under the
main website.

The dashboards invoked the backend api and loaded cms content (as json) from the main website.

Trainers uploaded video/image content via the trainer dashboard to google storage. As part of this a post was also made to the api
with the upload meta-data. Multiple files could be uploaded at a time while showing upload progress. 

Admins previewed this content via their own dashboard and could modify meta-data on the videos. They would then approve, reject, message
trainers. (I also created a site messaging system)

Liked/watched statistics for each video was requested by the main site from the api. This data wes cached on the server side
in the firebase database.

Trainer content (bio's, profile images etc) when approved was published to git and caused a new static site build (hugo). The 
firebase api directly interacted with github to create a branch, add content and publish. This would kick off a netlify build.

### Problems solved

It worked and the MVP was deployed.

Members could signup, like and follow videos
Trainers could signup and upload videos and profile information
Admin's could review the content submissions and approve/modify/revoke them
Admins could invoke the publishing of videos from the dashboard 

### Issues encountered

Many. 

1. The main site was built using jquery and javascript. It was poorly designed and was a mess. This was a pain to work on
   and broke very easily. Most of the javascript was in about 3 files. Then hugo had their own scripting language and things 
   were all over the place.
   - this was a big time sink each time we touched it
2. Clients kept changing their mind and then denied it. Not really a technical issue but it did make planning a design hard. Rather
   than a well thought out plan it was more a reactionary approach. There was little pushback from the project owner. 
3. Netlify builds took a while. Articles were published to application development branches meaning that one content update
   there'd be a whole lot of merges (and potential conflict) 
4. Main site login code was flaky and we had timing issues (things would sometime work, then not other times)
5. Incredibly short deadline so lot's of pressure and shortcuts were made, leading to more delays as we tripped over bugs.
6. Vimio push uploads timed out, so hence the google storage option. 
7. Our New Zealand team failed to produce anything. So more pressure on us.

Incredibly the other devs picked up VueJs and typescript quickly and decided to never use jquery again. I was surprised
how quickly they become productive in it. 

### Things to do differently

1. Say no to the project.
2. Split the application code from the CMS published code. A site deploy should link to a versioned cdn delivered 
   application bundle to merge with the CMS content. This would significantly speed up build times (to avoid rebuilding
   code that doesn't need rebuilding)
3. Separate repo for the dashboards, so the dashboards could be built and tested without a full site build. And again 
   linked via a versioned cdn bundle.
4. Just use the vimio pull upload mechanism (invoke vimio, they would suck up our video/content). 
    - Or use fastly and VSL/lucet to serve video content and provide access control (something I investigated)
5. Not much could have been done about the main site except rewrite in VueJs. Would have probably been faster than the 
   time it took to deal with issues.
     - possibly use Nuxt (uses VueJs) for static site generation instead of hugo. 
6. Insist we down tools and come up with requirements and design before carrying on work.
   - this includes a data interaction design and not just front-end
7. Obviously video upload and content delivery via Google Storage didn't perform any video conversion, meta-data stripping,
virus checking.
8. Insist on writing at least some tests, especially around login and perms.    

### Nice things

The dashboards, api design was clean and that aspect of development was fast (once it was clear what was needed).







    
    







  



