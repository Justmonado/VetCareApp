const mongoose = require('mongoose');

const mascotaSchema = new mongoose.Schema({
    nombre:{
        type:String,
        required:true
    },
    especie:{
        type:String,
        required:true
    },
    raza:{
        type:String,
        required:true
    },
    sexo:{
        type:String,
        required:true,
        enum:[
            "Macho",
            "Hembra"
        ]
    },
    edad:{
        type:Number,
        required:true
    },
    descripcion:{
        type:String,
        required:true
    },
    photoUrl:{
        type:String,
        required:true
    }
});

module.exports =
    mongoose.models.Mascota ||
    mongoose.model("Mascota", mascotaSchema);