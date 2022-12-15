const menus = [
    {
        id: 1,
        name: 'Home',
        links: '/home-03',
    },
    {
        id: 2,
        name: 'Marketplace',
        links: '/explore-03',
    },
    {
        id: 4,
        name: "Account",
        links: "/login",
        namesub: [
            {
                id: 1,
                sub: 'Login',
                links: '/login'
            },
            {
                id: 2,
                sub: 'Sign-up',
                links: '/sign-up'
            }
        ],
    },
    {
        id: 5,
        name: 'Contact',
        links: '/contact-01',
    },
    
]

export default menus;