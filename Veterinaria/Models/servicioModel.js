const mongoose = require('mongoose');

const servicioSchema = new mongoose.Schema({
    sucursalId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: "Sucursal",
        required: true
    },
    nombre:{
        type:String,
        required:true,
        enum:[
            "Consulta Medica",
            "Estetica",
            "Hotel",
            "Adopcion"
        ]
    },
    horarios:[{
        fecha:{
            type:Date,
            required:true
        },
        hora:{
            type:String,
            required:true
        }
    }],
    disponible:{
        type:Boolean,
        default:true
    }
});

module.exports = mongoose.model("Servicio", servicioSchema);