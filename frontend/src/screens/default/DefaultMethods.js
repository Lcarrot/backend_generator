export const DefaultMethods = {

    addByClick(prevValues, initVal, setValues) {
        return (event) => {
            event.preventDefault()
            const values = [...prevValues]
            values.push(initVal)
            setValues(values)
        }
    },

    defaultArrayChangeHandler(prevValues, setValues) {
        return (form, index) => {
            const newData = [...prevValues]
            newData[index] = form
            setValues(newData)
        }
    }
}