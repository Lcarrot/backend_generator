import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

function DefaultHeader({pageName, children}) {
    return (
        <>
            <Navbar collapseOnSelect expand="sm" bg="dark" variant="dark">
                <Navbar.Brand><h1 style={{color: 'whitesmoke'}}>{pageName} </h1></Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav"/>
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav>
                    </Nav>
                    <Nav className="justify-content-end flex-grow-1 pe-3">
                        {children}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        </>)
}

export default DefaultHeader;