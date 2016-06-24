# HeadlessFragments
Headless Fragments : Headless fragments are fragments without UI.
This example is to show how to use headless fragment for making network api calls and headling the state while activity rotation.
So even when the api call is progress and activity being recreated due to rotation there would be no new network calls again,
The previouly fetched data would be sent to activity as it is retained by the DataKeepFragments.

It also prevents memory leaks even while using AsyncTask.
