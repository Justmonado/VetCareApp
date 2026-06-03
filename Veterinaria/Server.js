const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
require('dotenv').config();

const app = express();

app.use(cors());
app.use(express.json());



mongoose.connect(process.env.MONGO_URI)
    .then(()=> console.log("Conectado a Mongo"))
    .catch(err=> console.log(err));

app.use("/api/auth", require('./routes/auth'));
app.use("/api/user", require('./routes/user'));


app.use("/api/sucursales", require('./routes/sucursales'));
app.use("/api/servicios", require('./routes/servicios'));
app.use("/api/mascotas", require('./routes/mascotas'));

app.use("/api/citas", require('./routes/citas'));
app.use("/api/adopcion", require('./routes/adopcion'));

app.listen(process.env.PORT,()=>{
    console.log("Servidor en el puerto" + process.env.PORT);
}); 