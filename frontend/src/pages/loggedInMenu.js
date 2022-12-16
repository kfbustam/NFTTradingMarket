const loggedInMenu = [
    {
        id: 1,
        name: 'Home',
        links: '/home-03',
    },
    {
        id: 2,
        name: 'Marketplace',
        links: '/explore-02',
    },
    {
        id: 3,
        name: 'Functionalities',
        links: '#',
        namesub: [
            {
                id: 1,
                sub: 'My Listings',
                links: '/activity-01'
            },
            {
                id: 2,
                sub: 'My Transactions',
                links: '/activity-02'
            },
            {
                id: 3,
                sub: 'Deposit Crypto',
                links: '/deposits'
            },
            {
                id: 4,
                sub: 'Withdraw Crypto',
                links: '/withdrawal'
            }
        ],
    },
    {
        id: 4,
        name: "Logout",
        links: "/logout",
    },
    {
        id: 5,
        name: 'Contact',
        links: '/contact-01',
    },
    
]

export default loggedInMenu;