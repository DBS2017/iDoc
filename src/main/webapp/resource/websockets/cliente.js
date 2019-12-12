var socket = new WebSocket('ws://' + location.hostname + (location.port ? ':' + location.port : '') + '/iDoc/ws/server');

function enviar(mensaje) {
    socket.send(mensaje);
}

socket.onopen = function (event) {
    console.log('Conexión abierta ' + event);
};

socket.onmessage = function (event) {
    notificarActualizar(event);
};

socket.onclose = function (event) {
    console.log('Conexión socket cerrada ' + event);
};

socket.onerror = function (event) {
    console.log('Error! ' + event);
};

function cerrarSesion() {
    socket.onclose = function () {};
    socket.close();
    console.log('Conexión socket cerrada!!');
}


function notificarActualizar(event) {
    PF('mensajeSockets').removeAll();
    PF('mensajeSockets').renderMessage({
        "summary": "Hubierón cambios recientes",
        "detail": event.data,
        "severity": "info",
        "life": "200"
    });
    switch (event.data) {
        case "Persona":
            actualizarPersona();
            break;
        case "Area":
            actualizarArea();
            break;
        case "Documento":
            actualizarDocumento();
            break;
        case "Empresa":
            actualizarEmpresa();
            break;
        case "IncidenciaTipo":
            actualizarIncidenciaTipo();
            break;
        case "Trabajador":
            actualizarTrabajador();
            break;
        case "Transferencia":
            actualizarTransferencia();
            break;
        case "Tupa":
            actualizarTupa();
            break;
        case "Sugerencia":
            actualizarSugerencia();
            break;
        case "DocumentoTipo":
            actualizarDocumentoTipo();
            break;
        case "Bandeja":
            actualizarBandeja();
            break;
        case "Acta":
            actualizarActa();
            break;
        default:
            break;
    }

//    location.reload();
}


function actualizarPersona() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarPersona", f: "form", u: "frmRegistrarPersona frmListarPersona", onco: function (xhr, status, args) {
                PF('wvDtTblPersona').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarArea() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarArea", f: "form", u: "frmRegistrarArea frmListarArea", onco: function (xhr, status, args) {
                PF('wvListaArea').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarActa() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarActa", f: "form", u: "frmRegistrarActa frmActa", onco: function (xhr, status, args) {
                PF('wvTblActa').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarDocumento() {

}

function actualizarDocumentoTipo() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarDocumentoTipo", f: "form", u: "FormDtipo FormTblDtipo"});
        return false;
    } catch (e) {
    }
}

function actualizarIncidenciaTipo() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarIncidenciaTipo", f: "form", u: "frmRegistrarIncidenciaTipo frmListarIncidenciaTipo", onco: function (xhr, status, args) {
                PF('wvDtTblIncidenciaTipo').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarTrabajador() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarTrabajador", f: "form", u: "frmRegistrarTrabajador frmListarTrabajador", onco: function (xhr, status, args) {
                PF('wdtTblTrabajador').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarTransferencia() {

}

function actualizarTupa() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarTupa", f: "form", u: "FormTupa FormTblTupa", onco: function (xhr, status, args) {
                PF('wvTblTupa').filter();
                ;
            }});
        return false;
    } catch (e) {
    }
}

function actualizarEmpresa() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarEmpresa", f: "form", u: "FormEmpresa FormTblEmpresa"});
        return false;
    } catch (e) {
    }
}

function actualizarSugerencia() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarSugerencia", f: "form", p: "form:btnActualizarSugerencia frmSugerencia", u: "frmSugerencia"});
        return false;
    } catch (e) {
    }
}

function actualizarBandeja() {
    try {
        PrimeFaces.ab({s: "form:btnActualizarBandeja", f: "form", u: "bandejaEntrada bandejaSalida", onco: function (xhr, status, args) {
                PF('wvListaBandejaEntrada').filter();
                PF('wvListaBandejaSalida').filter();
                ;
            }});
        return false;
    } catch (e) {
        console.log('Erros ws ' + e);
    }
}