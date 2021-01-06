import JobFooter from "./jobFooter";
import {BrowserRouter, Route} from "react-router-dom";
import React from "react";
import JobMenu from "./JobMenu";
import Login from "../pages/login/login";

function Container() {
    return (
        <div className="container">
            <BrowserRouter>
                <Route path={'/login'} component={Login}/>
                <Route path={'/app'} component={JobMenu}/>
            </BrowserRouter>
            <JobFooter/>
        </div>
    )
}

export default Container;
