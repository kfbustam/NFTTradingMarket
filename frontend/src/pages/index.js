import Home03 from "./Home03";
import Browse from "./Browse";

import LiveAuctions from "./LiveAuctions";
import ItemDetails01 from "./ItemDetails01";
import ItemDetails02 from "./ItemDetails02";
import Listings from "./Listings";
import Activity02 from "./Activity02";
import Blog from "./Blog";
import BlogDetails from "./BlogDetails";
import HelpCenter from "./HelpCenter";
import Authors01 from "./Authors01";
import Authors02 from "./Authors02";
import WalletConnect from "./WalletConnect";
import CreateItem from "./CreateItem";
import EditProfile from "./EditProfile";
import Ranking from "./Ranking";
import Login from "./Login";
import Logout from "./Logout";
import SignUp from "./SignUp";
import NoResult from "./NoResult";
import FAQ from "./FAQ";
import Contact01 from "./Contact01";
import Contact02 from "./Contact02";
import Deposits from "./Deposits";
import Withdrawal from "./Withdrawal";
import SystemDashboard from "./SystemDashboard";
import ListItem from "./ListItem";
import MyNfts from "./MyNfts";


const routes = [
  // { path: '/', component: <Home01 />},
  { path: '/', component: <Home03 />},
  { path: '/home-03', component: <Home03 />},
  { path: '/browse', component: <Browse />},
  { path: '/live-auctions', component: <LiveAuctions />},
  { path: '/item-details-01', component: <ItemDetails01 />},
  { path: '/item-details-02', component: <ItemDetails02 />},
  { path: '/my-listings', component: <Listings />},
  { path: '/transactions', component: <Activity02 />},
  { path: '/my-nfts', component: <MyNfts />},
  { path: '/blog', component: <Blog />},
  { path: '/blog-details', component: <BlogDetails />},
  { path: '/help-center', component: <HelpCenter />},
  { path: '/authors-01', component: <Authors01 />},
  { path: '/authors-02', component: <Authors02 />},
  { path: '/wallet-connect', component: <WalletConnect />},
  { path: '/create-item', component: <CreateItem />},
  { path: '/list-item', component: <ListItem />},
  { path: '/edit-profile', component: <EditProfile />},
  { path: '/ranking', component: <Ranking />},
  { path: '/login', component: <Login />},
  { path: '/logout', component: <Logout />},
  { path: '/sign-up', component: <SignUp />},
  { path: '/no-result', component: <NoResult />},
  { path: '/faq', component: <FAQ />},
  { path: '/contact-01', component: <Contact01 />},
  { path: '/contact-02', component: <Contact02 />},
  { path: '/deposits', component: <Deposits />},
  { path: '/withdrawal', component: <Withdrawal />},
  { path: '/stats', component: <SystemDashboard />}
]

export default routes;