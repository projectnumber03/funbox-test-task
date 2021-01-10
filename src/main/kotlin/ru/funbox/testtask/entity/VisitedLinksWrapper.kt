package ru.funbox.testtask.entity

class VisitedLinksWrapper(
        val id: Long = System.currentTimeMillis(),
        val links: ArrayList<String> = ArrayList()
)