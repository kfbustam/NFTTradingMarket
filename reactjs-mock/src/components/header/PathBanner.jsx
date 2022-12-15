import React , { useState } from 'react';
import { Link } from 'react-router-dom';

const PathBanner = ({heading}) => {
    return(
        <section className="flat-title-page inner">
            <div className="overlay"></div>
            <div className="themesflat-container">
                <div className="row">
                    <div className="col-md-12">
                        <div className="page-title-heading mg-bt-12">
                            <h1 className="heading text-center">{heading}</h1>
                        </div>
                        <div className="breadcrumbs style2">
                            <ul>
                                <li><Link to="/">Home</Link></li>
                                <li>{heading}</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
}

export default PathBanner;