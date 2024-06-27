package nonabili.reviewserviceserver.util.error

class CustomError(val reason: ErrorState): RuntimeException(reason.message)