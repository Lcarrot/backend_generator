import Card from 'react-bootstrap/Card';

function CardWindow({titleName, children}) {
    return (
        <div>
            <Card style={{ width: '100%' }}>
                <Card.Body>
                    <Card.Title> {titleName} </Card.Title>
                    <Card.Text>
                        {children}
                    </Card.Text>
                </Card.Body>
            </Card>
        </div>)
}

export default CardWindow;