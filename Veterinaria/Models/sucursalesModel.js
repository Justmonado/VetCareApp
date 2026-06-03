const mongoose = require('mongoose');

const sucursalSchema = new mongoose.Schema({
    nombre:{
        type:String,
        required:true
    },
    direccion:{
        type:String,
        required:true
    },
    horario:{
        type:String,
        required:true
    },
    photoUrl:{
        type:String,
        required:true
    },
    servicios:[{
        type:String,
        enum:[
            "Consulta Medica",
            "Estetica",
            "Hotel",
            "Adopcion"
        ]
    }]
});

module.exports =
    mongoose.models.Sucursal ||
    mongoose.model("Sucursal", sucursalSchema);