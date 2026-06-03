const mongoose = require('mongoose');

const citaSchema = new mongoose.Schema({
    usuarioId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Usuario',
        required: true
    },
    nombreMascota:{
        type:String,
        required:true
    },
    nombrePersona:{
        type:String,
        required:true
    },
    fecha:{
        type:Date,
        required:true
    },
    hora:{
        type:String,
        required:true
    },
    servicio:{
        type:String,
        required:true
    },
    sucursalId:{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Sucursal',
        required: true
    }
});

module.exports = mongoose.model("Cita", citaSchema);